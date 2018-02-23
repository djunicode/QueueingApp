package com.djunicode.queuingapp.rest;

import com.djunicode.queuingapp.model.LocationTeacher;
import com.djunicode.queuingapp.model.Student;
import com.djunicode.queuingapp.model.StudentForId;
import com.djunicode.queuingapp.model.StudentQueue;
import com.djunicode.queuingapp.model.StudentSubscriptions;
import com.djunicode.queuingapp.model.UserEmailVerify;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by DELL_PC on 09-01-2018.
 */

public interface ApiInterface {

  @FormUrlEncoded
  @POST("queues/student/")
  Call<Student> createStudentAccount(@Field("user") int user_id, @Field("name") String username,
      @Field("sapID") String sapId, @Field("department") String department,
      @Field("year") String year, @Field("batch") String batch);

  @FormUrlEncoded
  @POST("queues/users/")
  Call<StudentForId> getId(@Field("username") String username, @Field("password") String password);

  @FormUrlEncoded
  @POST("queues/")
  Call<LocationTeacher> sendTeacherLocation(@Field("floor") Integer floor,
      @Field("department") String department, @Field("room") String room);

  @GET("queues/{id}/")
  Call<LocationTeacher> getTeacherLocation(@Path("id") int id);

  @DELETE("queues/{id}/")
  Call<LocationTeacher> deleteTeacherLocation(@Path("id") int id);

  @FormUrlEncoded
  @POST("queues/users/")
  Call<UserEmailVerify> sendEmail(@Field("username") String name, @Field("email") String email,
      @Field("password") String password);

  @FormUrlEncoded
  @PUT("queues/users/{id}/")
  Call<UserEmailVerify> verifyEmail(@Path("id") int id, @Field("token") String token);

  @FormUrlEncoded
  @PUT("queues/queue/{id}/")
  Call<StudentQueue> studentJoiningTheQueue (@Path("id") int id, @Field("queueItems") String sapID);

  @GET("queues/queue/{id}/")
  Call<StudentQueue> getUpdatedQueue(@Path("id") int id);

  @FormUrlEncoded
  @PUT("queues/student/{id}/")
  Call<Student> setStudentSubscriptions(@Path("id") int id, @Field("teacherNames")
      List<String> subscriptions);
}
