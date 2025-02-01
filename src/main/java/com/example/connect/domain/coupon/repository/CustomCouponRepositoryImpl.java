package com.example.connect.domain.coupon.repository;

import com.example.connect.domain.coupon.dto.CouponResDto;
import com.example.connect.domain.coupon.entity.QCoupon;
import com.example.connect.global.enums.CouponFilter;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomCouponRepositoryImpl implements CustomCouponRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CouponResDto> findByOrderByCreatedAtDesc(CouponFilter filter, Pageable pageable) {
        QCoupon c = QCoupon.coupon;
        BooleanBuilder builder = new BooleanBuilder();

        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        builder.and(c.count.gt(0));

        if (Objects.equals(filter, CouponFilter.ISSUED_COUPON)) {
            builder.and(c.openDate.before(LocalDateTime.now()).or(c.openDate.eq(LocalDateTime.now())));
            orderSpecifiers.add(new OrderSpecifier<>(Order.ASC, c.expiredDate));
        } else if (Objects.equals(filter, CouponFilter.WAITING_COUPON)) {
            builder.and(c.openDate.after(LocalDateTime.now()));
            orderSpecifiers.add(new OrderSpecifier<>(Order.ASC, c.openDate));
        }

        orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, c.createdAt));
        
        List<CouponResDto> coupons = queryFactory.select(Projections.constructor(CouponResDto.class, c))
                .from(c)
                .where(builder)
                .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(queryFactory.select(c.count())
                .from(c)
                .where(builder)
                .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(coupons, pageable, total);
    }

}
