package com.example.bicoccahelp.ui.welcome;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.auth.AuthRepository;
import com.example.bicoccahelp.data.user.UserModel;
import com.example.bicoccahelp.data.user.UserRepository;


public class WelcomeViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    private final MutableLiveData<Boolean> loginSuccess;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<Boolean> emailSent;
    private final MutableLiveData<Boolean> emailVerified;
    private final MutableLiveData<String> errorMessage;

    public WelcomeViewModel(AuthRepository authRepository, UserRepository userRepository){
        this.authRepository = authRepository;
        this.userRepository = userRepository;

        this.loginSuccess = new MutableLiveData<>();
        this.emailSent  = new MutableLiveData<>();
        this.emailVerified = new MutableLiveData<>();
        this.errorMessage  = new MutableLiveData<>();
        this.isLoading = new MutableLiveData<>();
    }

    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getEmailSent() {
        return emailSent;
    }

    public LiveData<Boolean> getEmailVerified() {
        return emailVerified;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void login(String email, String password){
        isLoading.setValue(true);
        authRepository.login(email, password, new Callback<Void>() {
            @Override
            public void onSucces(Void unused) {
                userRepository.reload(new Callback<UserModel>() {
                    @Override
                    public void onSucces(UserModel userModel) {
                        loginSuccess.setValue(true);
                        isLoading.setValue(false);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        errorMessage.setValue("Login failed");
                        isLoading.setValue(false);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Invalid email or password");
                isLoading.setValue(false);
            }
        });
    }

    public void register(String email, String password, String name){
        authRepository.register(email, password, new Callback<Void>() {
            @Override
            public void onSucces(Void unused) {
                updateUsername(name);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Registration failed");
            }
        });
    }

    public void updateUsername(String name) {
        userRepository.updateUsername(name, new Callback<Void>() {
            @Override
            public void onSucces(Void unused) {
                sendEmailVerification();
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Failed to set username: " + e.getMessage());
            }
        });
    }
    
    public void sendEmailVerification(){
        userRepository.sendEmailVerification(new Callback<Void>() {
            @Override
            public void onSucces(Void unused) {
                emailSent.setValue(true);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Failed to send email verification");
            }
        });
    }
    
    public void verifyEmail(){
        userRepository.reload(new Callback<UserModel>() {
            @Override
            public void onSucces(UserModel userModel) {
                emailVerified.setValue(userModel.isEmailVerified());
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Failed to verify email");
            }
        });
    }

    public void resendEmail(){
        userRepository.reload(new Callback<UserModel>() {
            @Override
            public void onSucces(UserModel userModel) {
                if(userModel.isEmailVerified()){
                    emailVerified.setValue(true);
                    errorMessage.setValue("Email verified");
                }else{
                    userRepository.sendEmailVerification(new Callback<Void>() {
                        @Override
                        public void onSucces(Void unused) {
                            errorMessage.setValue("Verification email sent");
                        }

                        @Override
                        public void onFailure(Exception e) {
                            errorMessage.setValue("Email not sent");
                        }
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Failed to reload user");
            }
        });
    }
    
    public void forgotPassword(String email){
        authRepository.forgotPassword(email, new Callback<Void>() {
            @Override
            public void onSucces(Void unused) {
                emailSent.setValue(true);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Failed to send password reset email");
            }
        });
    }



}
