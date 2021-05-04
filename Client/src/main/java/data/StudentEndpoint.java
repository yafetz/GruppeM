package data;

import entities.Student;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface StudentEndpoint {

    @GET("student/{id}")
    Call<Student> getStudentById(@Path("id") Integer id);

    @GET("student/all")
    Call <List<Student>> getAllStudents();
}
