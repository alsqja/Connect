package com.example.connect.domain.couponuser.repository;

import com.example.connect.domain.coupon.entity.QCoupon;
import com.example.connect.domain.couponuser.dto.CouponUserResDto;
import com.example.connect.domain.couponuser.entity.QCouponUser;
import com.example.connect.domain.user.entity.QUser;
import com.example.connect.global.enums.CouponUserStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomCouponUserRepositoryImpl implements CustomCouponUserRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CouponUserResDto> findCouponUserByUserId(Long userId, CouponUserStatus status, Pageable pageable) {
        QCouponUser cu = QCouponUser.couponUser;
        QCoupon c = QCoupon.coupon;
        QUser u = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(u.id.eq(userId));
        if (status != CouponUserStatus.ALL) {
            builder.and(cu.status.eq(status));
        }

        List<CouponUserResDto> results = queryFactory.select(
                        Projections.constructor(CouponUserResDto.class, cu.id, u.id, c.id, c.name, c.description, c.expiredDate, cu.status, cu.createdAt, cu.updatedAt))
                .from(cu)
                .join(cu.coupon, c)
                .join(cu.user, u)
                .where(builder)
                .orderBy(cu.status.asc(), c.expiredDate.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(queryFactory.select(cu.count())
                .from(cu)
                .join(cu.coupon, c)
                .join(cu.user, u)
                .where(builder)
                .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(results, pageable, total);
    }
}
