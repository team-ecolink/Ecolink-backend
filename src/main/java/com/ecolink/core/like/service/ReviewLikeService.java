package com.ecolink.core.like.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecolink.core.avatar.domain.Avatar;
import com.ecolink.core.avatar.service.AvatarService;
import com.ecolink.core.common.error.ErrorCode;
import com.ecolink.core.common.error.exception.ReviewLikeAlreadyExistsException;
import com.ecolink.core.like.domain.ReviewLike;
import com.ecolink.core.like.repository.ReviewLikeRepository;
import com.ecolink.core.review.dto.response.GetReviewResponse;
import com.ecolink.core.review.domain.Review;
import com.ecolink.core.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewLikeService {

	private final ReviewLikeRepository reviewLikeRepository;
	private final AvatarService avatarService;
	private final ReviewService reviewService;

	public Page<GetReviewResponse> findReviewLike(Page<GetReviewResponse> reviews, Long avatarId) {
		Set<ReviewLike> reviewLikes = new HashSet<>(reviewLikeRepository.findAllByReviewList(
			reviews.map(GetReviewResponse::getId).toList(), avatarId));

		Set<Long> reviewIds = reviewLikes.stream().map(reviewLike ->
			reviewLike.getReview().getId()).collect(Collectors.toSet());

		reviews.stream().filter(response -> reviewIds.contains(response.getId()))
			.forEach(GetReviewResponse::setLikedTrue);
		return reviews;
	}

	public boolean existsReviewLike(Long avatarId, Long reviewId) {
		return reviewLikeRepository.existsByAvatarAndReview(avatarId, reviewId);
	}
	@Transactional
	public ReviewLike addReviewLike(Long reviewId, Long avatarId) {
		Avatar avatar = avatarService.getById(avatarId);
		Review review = reviewService.getById(reviewId);

		if (existsReviewLike(avatarId, reviewId)) {
			throw new ReviewLikeAlreadyExistsException(ErrorCode.REVIEWLIKE_ALREADY_EXISTS);
		}

		ReviewLike reviewLike = new ReviewLike(review, avatar);
		ReviewLike savedReviewLike = reviewLikeRepository.save(reviewLike);

		review.addReviewLikeCount();

		return savedReviewLike;
	}

}
