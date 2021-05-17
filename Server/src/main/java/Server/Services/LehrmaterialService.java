package Server.Services;

import Server.Modell.Lehrmaterial;
import Server.Repository.LehrmaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LehrmaterialService {
    LehrmaterialRepository lehrmaterialRepository;
    @Autowired
    public LehrmaterialService (LehrmaterialRepository lehrmaterialRepository) {
        this.lehrmaterialRepository = lehrmaterialRepository;

    }

    public List<Lehrmaterial> teachingCourses(long id) {
        return lehrmaterialRepository.getTeachingCoursesById(id);

    }
}
