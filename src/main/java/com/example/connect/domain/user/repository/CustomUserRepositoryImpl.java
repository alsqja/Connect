package com.example.connect.domain.user.repository;

import com.example.connect.domain.membership.entity.QMembership;
import com.example.connect.domain.user.dto.AdminUserResDto;
import com.example.connect.domain.user.entity.QUserAdminOnly;
import com.example.connect.domain.user.entity.UserAdminOnly;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.NotFoundException;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AdminUserResDto> findQueryAdminUserDetails(Pageable pageable) {

        QUserAdminOnly user = QUserAdminOnly.userAdminOnly;
        QMembership membership = QMembership.membership;

        List<AdminUserResDto> results = queryFactory.select(
                        Projections.constructor(
                                AdminUserResDto.class,
                                user.id,
                                user.name,
                                user.birth,
                                user.gender,
                                user.profileUrl,
                                user.status,
                                user.isActiveMatching,
                                user.role,
                                membership.type,
                                user.reportedCount,
                                membership.expiredDate,
                                user.createdAt,
                                user.updatedAt,
                                user.isDeleted
                        )
                )
                .from(user)
                .leftJoin(membership).on(user.id.eq(membership.user.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(
                queryFactory.select(user.count())
                        .from(user)
                        .fetchOne()
        ).orElse(0L);


        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public UserAdminOnly findByIdWithDeleted(Long id) {

        QUserAdminOnly user = QUserAdminOnly.userAdminOnly;

        UserAdminOnly foundUser = queryFactory.selectFrom(user)
                .where(user.id.eq(id))
                .fetchOne();

        if (foundUser == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }

        return foundUser;
    }
}
