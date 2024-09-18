package com.example.bicoccahelp.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.auth.AuthRepository;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.student.StudentRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;

import org.checkerframework.checker.units.qual.C;

public class ProfileViewModel extends ViewModel {

        private final AuthRepository authRepository;
        private final UserRepository userRepository;
        private final StudentRepository studentRepository;
        private final TutorRepository tutorRepository;

        private final MutableLiveData<Boolean> nameUpdated;
        private final MutableLiveData<Boolean> passwordUpdated;
        private MutableLiveData<Boolean> studentExists;
        private final MutableLiveData<Boolean> isLogOut;
        private final MutableLiveData<Boolean> isDeleted;
        private final MutableLiveData<String> errorMessage;


        public ProfileViewModel(AuthRepository authRepository, UserRepository userRepository,
                                StudentRepository studentRepository,
                                TutorRepository tutorRepository){
            this.authRepository = authRepository;
            this.userRepository = userRepository;
            this.studentRepository = studentRepository;
            this.tutorRepository = tutorRepository;

            this.isDeleted = new MutableLiveData<>();
            this.isLogOut = new MutableLiveData<>();
            this.passwordUpdated = new MutableLiveData<>();
            this.nameUpdated = new MutableLiveData<>();
            this.errorMessage = new MutableLiveData<>();
            this.studentExists = new MutableLiveData<>();
        }


        public LiveData<Boolean> getStudentExists() {
            return studentExists;
        }

    public LiveData<Boolean> getNameUpdated() {
            return nameUpdated;
        }

        public LiveData<String> getErrorMessage() {
            return errorMessage;
        }

        public LiveData<Boolean> getPasswordUpdated(){
            return  passwordUpdated;
        }

        public LiveData<Boolean> getLogOut(){
            return isLogOut;
        }

        public LiveData<Boolean> getDeleted(){
            return isDeleted;
        }

        public void updateName(String name){
            String userUid = userRepository.getCurrentUser().getUid();

            userRepository.updateUsername(name, new Callback<Void>() {
                @Override
                public void onSucces(Void unused) {
                    isTutor(userUid, name);
                }

                @Override
                public void onFailure(Exception e) {
                    errorMessage.setValue("Name not updated");
                }
            });
        }

        public void isTutor(String userUid, String newName){
            studentRepository.isTutor(userUid, true, new Callback<Boolean>() {
                @Override
                public void onSucces(Boolean isTutor) {
                    if(isTutor){
                        tutorRepository.updateTutorName(userUid, newName);
                    }

                    studentRepository.updateStudentName(userUid, newName);
                    nameUpdated.setValue(true);
                }

                @Override
                public void onFailure(Exception e) {
                    errorMessage.setValue("Name not updated");
                }
            });
        }

        public void updatePassword(String oldPassword, String newPassword){

            userRepository.reauthenticate(oldPassword, new Callback<Void>() {
                @Override
                public void onSucces(Void unused) {
                    changePassword(newPassword);
                }

                @Override
                public void onFailure(Exception e) {
                    errorMessage.setValue("Unable to reauthenticate");
                }
            });

        }

        public void changePassword(String newPassword){
            userRepository.updatePassword(newPassword, new Callback<Void>() {
                @Override
                public void onSucces(Void unused) {
                    passwordUpdated.setValue(true);
                    authRepository.logout();
                }

                @Override
                public void onFailure(Exception e) {
                    errorMessage.setValue("Error: Password Not Updated");
                }
            });
        }

        public void logOut(){
            authRepository.logout();
            isLogOut.setValue(true);
        }

        public void deleteUser(String password){

            String userId = userRepository.getCurrentUser().getUid();
            userRepository.reauthenticate(password, new Callback<Void>() {
                @Override
                public void onSucces(Void unused) {
                    deleteProfile(userId);
                }

                @Override
                public void onFailure(Exception e) {
                    errorMessage.setValue("Invalid Password Entered");
                }
            });
        }

        public void deleteProfile(String userId){
            userRepository.deleteUser(new Callback<Void>() {
                @Override
                public void onSucces(Void unused) {
                    isDeleted.setValue(true);
                    isTutorAndDelete(userId);

                }

                @Override
                public void onFailure(Exception e) {
                    errorMessage.setValue("Error: user not deleted");
                }
            });
        }

        public void isTutorAndDelete(String userId){
            studentRepository.isTutor(userId, true, new Callback<Boolean>() {
                @Override
                public void onSucces(Boolean isTutor) {
                    if(isTutor){
                        tutorRepository.deleteTutor(userId);
                    }

                    studentRepository.deleteStudent(userId);

                }

                @Override
                public void onFailure(Exception e) {
                    errorMessage.setValue("Error: Student/Tutor not deleted");
                }
            });
        }

    public void checkStudentExists() {
        String uid = userRepository.getCurrentUser().getUid();

        studentRepository.studentExist(uid, new Callback<Boolean>() {
            @Override
            public void onSucces(Boolean exist) {
                studentExists.setValue(exist);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue(e.getMessage());
            }
        });
    }



}
