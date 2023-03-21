package project.bookservice.service;

import project.bookservice.domain.starRating.StarRating;

public interface StarRatingService {

    StarRating saveStarRating(StarRating starRating);

    Double findAvgStarRating(String isbn);

    Boolean findByUserId(StarRating starRating);
}
