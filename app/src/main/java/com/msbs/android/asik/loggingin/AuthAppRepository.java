package com.msbs.android.asik.loggingin;

import android.app.Application;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.msbs.android.asik.model.AppDatabase;
import com.msbs.android.asik.model.AppExecutors;
import com.msbs.android.asik.model.User;

public class AuthAppRepository {

    private Application application;
    private UserDao mUserDao;


    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private User user;


    public AuthAppRepository(Application application) {
        this.application = application;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userLiveData = new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();

        AppDatabase mDB = AppDatabase.getInstance(application);
        mUserDao = mDB.userDao();


        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.postValue(firebaseAuth.getCurrentUser());
            loggedOutLiveData.postValue(false);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(application.getMainExecutor(), task -> {
                    if (task.isSuccessful()) {
                        userLiveData.postValue(firebaseAuth.getCurrentUser());

                    } else {
                        Toast.makeText(application.getApplicationContext(), "Login Failure: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void register(String email, String password) {
        MutableLiveData<User> authenticatedUserMutableLiveData = new MutableLiveData<>();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userLiveData.postValue(firebaseAuth.getCurrentUser());
                            boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                String userId = firebaseUser.getUid();
                                String firstname = firebaseUser.getDisplayName();
                                String email = firebaseUser.getEmail();
                                User user = new User(userId, email, null, null, null, null, null, null,null, null);
                                user.isNew = isNewUser;

                                AppExecutors.getInstance().diskIO().execute(new Runnable()
                                {
                                    @Override
                                    public void run() {

                                        mUserDao.insertTask(user);
                                    }
                                });authenticatedUserMutableLiveData.setValue(user);
                            }
                        } else {
                            Toast.makeText(application.getApplicationContext(), "Registration Failure: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    public void logOut() {
        firebaseAuth.signOut();
        loggedOutLiveData.postValue(true);
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insertTask(User user) {

        AppDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.insertTask(user);
        });
    }
}

