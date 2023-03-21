package project.bookservice.repository.starRating;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.bookservice.domain.starRating.StarRating;

@Slf4j
@RequiredArgsConstructor
@Repository
public class MybatisStarRatingRepository implements StarRatingRepository{

    private final StarRatingMapper starRatingMapper;
    @Override
    public StarRating saveStarRating(StarRating starRating) {
        log.info("StarRatingMapper class={}", starRating);
        starRatingMapper.saveStarRating(starRating);
        return starRating;
    }

    @Override
    public Double findAvgStarRating(String isbn) {
        return starRatingMapper.findAvgStarRating(isbn);
    }

    @Override
    public Boolean findByUserId(StarRating starRating) {
        return starRatingMapper.findByUserId(starRating);
    }
}
