package project.bookservice.repository.starRating;

import org.apache.ibatis.annotations.Mapper;
import project.bookservice.domain.starRating.StarRating;

@Mapper
public interface StarRatingMapper {

    void saveStarRating(StarRating starRating);

    Double findAvgStarRating(String isbn);

    Boolean findByUserId(StarRating starRating);
}
