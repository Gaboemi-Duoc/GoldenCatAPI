package alumno.duoc.golden_cat_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import alumno.duoc.golden_cat_api.model.Blog;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    
    @Query(value = "SELECT * FROM blogs WHERE writer = :writer ORDER BY id_blog", nativeQuery = true)
    List<Blog> findByWriter(@Param("writer") String writer);
    
    @Query(value = "SELECT * FROM blogs WHERE nombre ILIKE %:keyword% ORDER BY id_blog", nativeQuery = true)
    List<Blog> findByNombreContaining(@Param("keyword") String keyword);
    
    @Query(value = "SELECT * FROM blogs WHERE description ILIKE %:keyword% ORDER BY id_blog", nativeQuery = true)
    List<Blog> findByDescriptionContaining(@Param("keyword") String keyword);
}