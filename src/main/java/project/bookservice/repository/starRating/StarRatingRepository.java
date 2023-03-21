package project.bookservice.repository.starRating;

import project.bookservice.domain.starRating.StarRating;

public interface StarRatingRepository {

    StarRating saveStarRating(StarRating starRating);

    Double findAvgStarRating(String isbn);

    Boolean findByUserId(StarRating starRating);
}
