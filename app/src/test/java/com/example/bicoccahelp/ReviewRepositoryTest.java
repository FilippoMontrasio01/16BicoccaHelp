package com.example.bicoccahelp;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import android.os.Looper;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.review.CreateReviewRequest;
import com.example.bicoccahelp.data.review.ReviewModel;
import com.example.bicoccahelp.data.review.ReviewRemoteDataSource;
import com.example.bicoccahelp.data.review.ReviewRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ReviewRepositoryTest {

    @Mock
    private ReviewRemoteDataSource mockReviewRemoteDataSource;

    @Mock
    private Callback<ReviewModel> mockReviewCallback;

    @Mock
    private Callback<Boolean> mockBooleanCallback;

    @Mock
    private Callback<Double> mockDoubleCallback;

    @Mock
    private Callback<List<ReviewModel>> mockListReviewCallback;

    @Mock
    private Callback<Float> mockFloatCallback;

    private ReviewRepository reviewRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        reviewRepository = new ReviewRepository(mockReviewRemoteDataSource);
    }

    @Test
    public void testCreateReview() {
        CreateReviewRequest request = new CreateReviewRequest("tutorUid", "studentUid", 4.5);
        ReviewModel reviewModel = new ReviewModel("tutorUid", "studentUid", 4.5);

        // Simula il comportamento del metodo createReview
        doAnswer(invocation -> {
            Callback<ReviewModel> callback = invocation.getArgument(1);
            callback.onSucces(reviewModel);
            return null;
        }).when(mockReviewRemoteDataSource).createReview(any(CreateReviewRequest.class), any(Callback.class));

        // Chiama il metodo da testare
        reviewRepository.createReview(request, mockReviewCallback);

        // Verifica che il metodo createReview sia stato chiamato con i parametri corretti
        verify(mockReviewRemoteDataSource).createReview(eq(request), any(Callback.class));
    }

    @Test
    public void testCheckReview() {
        // Simula il comportamento del metodo checkReview
        doAnswer(invocation -> {
            Callback<Boolean> callback = invocation.getArgument(2);
            callback.onSucces(true);
            return null;
        }).when(mockReviewRemoteDataSource).checkReview(anyString(), anyString(), any(Callback.class));

        // Chiama il metodo da testare
        reviewRepository.checkReview("studentUid", "tutorUid", mockBooleanCallback);

        // Verifica che il metodo checkReview sia stato chiamato con i parametri corretti
        verify(mockReviewRemoteDataSource).checkReview(eq("studentUid"), eq("tutorUid"), any(Callback.class));
    }

    @Test
    public void testListReviewedTutors() {
        List<ReviewModel> reviews = new ArrayList<>();
        reviews.add(new ReviewModel("tutorUid", "studentUid", 4.5));

        // Simula il comportamento del metodo listReviewedTutors
        doAnswer(invocation -> {
            Callback<List<ReviewModel>> callback = invocation.getArgument(2);
            callback.onSucces(reviews);
            return null;
        }).when(mockReviewRemoteDataSource).listReviewsByStudent(anyString(), anyLong(), any(Callback.class));

        // Chiama il metodo da testare
        reviewRepository.listReviewedTutors("studentUid", 10L, mockListReviewCallback);

        // Verifica che il metodo listReviewsByStudent sia stato chiamato con i parametri corretti
        verify(mockReviewRemoteDataSource).listReviewsByStudent(eq("studentUid"), eq(10L), any(Callback.class));
    }

    @Test
    public void testGetAverageReview() {
        double averageRating = 4.5;

        // Simula il comportamento del metodo getAverageReview
        doAnswer(invocation -> {
            Callback<Double> callback = invocation.getArgument(1);
            callback.onSucces(averageRating);
            return null;
        }).when(mockReviewRemoteDataSource).getAverageReview(anyString(), any(Callback.class));

        // Chiama il metodo da testare
        reviewRepository.getAverageReview("tutorUid", mockDoubleCallback);

        // Verifica che il metodo getAverageReview sia stato chiamato con i parametri corretti
        verify(mockReviewRemoteDataSource).getAverageReview(eq("tutorUid"), any(Callback.class));
    }

    @Test
    public void testListReviews() {
        List<ReviewModel> reviews = new ArrayList<>();
        reviews.add(new ReviewModel("tutorUid", "studentUid", 4.5));

        // Simula il comportamento del metodo listReviews
        doAnswer(invocation -> {
            Callback<List<ReviewModel>> callback = invocation.getArgument(2);
            callback.onSucces(reviews);
            return null;
        }).when(mockReviewRemoteDataSource).listReviews(anyString(), anyInt(), any(Callback.class));

        // Chiama il metodo da testare
        reviewRepository.listReviews("tutorUid", 10, mockListReviewCallback);

        // Verifica che il metodo listReviews sia stato chiamato con i parametri corretti
        verify(mockReviewRemoteDataSource).listReviews(eq("tutorUid"), eq(10), any(Callback.class));
    }

    @Test
    public void testGetReview() {
        float reviewScore = 4.5f;

        // Simula il comportamento del metodo getReview
        doAnswer(invocation -> {
            Callback<Float> callback = invocation.getArgument(2);
            callback.onSucces(reviewScore);
            return null;
        }).when(mockReviewRemoteDataSource).getReview(anyString(), anyString(), any(Callback.class));

        // Chiama il metodo da testare
        reviewRepository.getReview("studentUid", "tutorUid", mockFloatCallback);

        // Verifica che il metodo getReview sia stato chiamato con i parametri corretti
        verify(mockReviewRemoteDataSource).getReview(eq("studentUid"), eq("tutorUid"), any(Callback.class));
    }
}

