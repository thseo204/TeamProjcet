package project.bookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.bookservice.domain.starRating.StarRating;
import project.bookservice.repository.starRating.StarRatingRepository;

@Service
@RequiredArgsConstructor
public class StarRatingServiceImpl implements StarRatingService{

    private final StarRatingRepository starRatingRepository;

    @Override
    public StarRating saveStarRating(StarRating starRating) {
        return starRatingRepository.saveStarRating(starRating);
    }

    @Override
    public Double findAvgStarRating(String isbn) {

        return starRatingRepository.findAvgStarRating(isbn);
    }

    @Override
    public Boolean findByUserId(StarRating starRating) {
        return starRatingRepository.findByUserId(starRating);
    }
}
