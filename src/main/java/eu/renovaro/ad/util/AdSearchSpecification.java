package eu.renovaro.ad.util;

import eu.renovaro.ad.domain.*;
import eu.renovaro.ad.domain.entity.Ad;
import eu.renovaro.ad.domain.entity.FreelancePackage;
import eu.renovaro.ad.domain.entity.PackageType;
import eu.renovaro.user.domain.RoleName;
import eu.renovaro.user.domain.entity.Role;
import eu.renovaro.user.domain.entity.UserRole;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AdSearchSpecification {

    public static Specification<Ad> matches(AdFilterRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Object, Object> serviceType = root.join("serviceType", JoinType.LEFT);
            Join<Object, Object> userProfile = root.join("userProfile", JoinType.LEFT);
            Join<Object, Object> user = userProfile.join("user", JoinType.LEFT);
            Join<Object, Object> localService = root.join("localService", JoinType.LEFT);

            predicates.add(cb.equal(root.get("adStatus").get("adStatusCode"), AdStatusCode.ACTIVE));

            if (request != null) {

                if (request.getSearch() != null && !request.getSearch().isBlank()) {
                    String like = "%" + request.getSearch().trim().toLowerCase() + "%";
                    predicates.add(cb.like(cb.lower(root.get("title")), like));
                }

                if (request.getRating() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(
                            userProfile.get("ratingAverage"),
                            request.getRating().doubleValue()
                    ));
                }

                if (request.getPublishedBy() != null) {
                    Subquery<Integer> existsRole = query.subquery(Integer.class);
                    Root<UserRole> ur = existsRole.from(UserRole.class);
                    Join<UserRole, Role> role = ur.join("role", JoinType.INNER);

                    RoleName wantedRole = (request.getPublishedBy() == ProviderRole.COMPANY)
                            ? RoleName.COMPANY
                            : RoleName.SPECIALIST;

                    existsRole.select(cb.literal(1));
                    existsRole.where(
                            cb.equal(ur.get("userId"), user.get("userId")),
                            cb.equal(role.get("roleName"), wantedRole)
                    );

                    predicates.add(cb.exists(existsRole));
                }

                Expression<BigDecimal> priceExpr = priceExpression(root, query, serviceType, localService, cb);
                Expression<BigDecimal> priceForFilter = cb.coalesce(priceExpr, new BigDecimal("999999999"));

                if (request.getBudgetMin() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(priceForFilter, BigDecimal.valueOf(request.getBudgetMin())));
                }
                if (request.getBudgetMax() != null) {
                    predicates.add(cb.lessThanOrEqualTo(priceForFilter, BigDecimal.valueOf(request.getBudgetMax())));
                }

                if (!Long.class.equals(query.getResultType()) && !long.class.equals(query.getResultType())) {
                    List<Order> orders = new ArrayList<>();

                    if (request.getSortBy() != null) {
                        switch (request.getSortBy()) {
                            case DATE_DESC -> orders.add(cb.desc(root.get("createdAt")));
                            case VIEWS_DESC -> {
                                orders.add(cb.desc(cb.coalesce(root.get("viewCount"), 0L)));
                                orders.add(cb.desc(root.get("createdAt")));
                            }
                            case PRICE_ASC -> orders.add(cb.asc(priceForFilter));
                            case PRICE_DESC -> orders.add(cb.desc(priceForFilter));
                        }
                    } else {
                        orders.add(cb.desc(root.get("createdAt")));
                    }

                    orders.add(cb.desc(root.get("adId")));
                    query.orderBy(orders);
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Expression<BigDecimal> priceExpression(
            Root<Ad> root,
            CriteriaQuery<?> query,
            Join<Object, Object> serviceType,
            Join<Object, Object> localService,
            CriteriaBuilder cb
    ) {
        Subquery<BigDecimal> basicPriceSub = query.subquery(BigDecimal.class);
        Root<FreelancePackage> fp = basicPriceSub.from(FreelancePackage.class);
        Join<FreelancePackage, PackageType> pt = fp.join("packageType", JoinType.INNER);

        basicPriceSub.select(cb.min(fp.get("packagePrice")));
        basicPriceSub.where(
                cb.equal(fp.get("ad"), root),
                cb.equal(pt.get("packageTypeCode"), PackageTypeCode.BASIC)
        );

        return cb.<BigDecimal>selectCase()
                .when(cb.equal(serviceType.get("serviceTypeCode"), ServiceTypeCode.FREELANCE), basicPriceSub)
                .otherwise(localService.get("servicePriceMin"));
    }
}
