package eu.renovaro.ad.util;

import eu.renovaro.ad.domain.*;
import eu.renovaro.ad.domain.entity.Ad;
import eu.renovaro.ad.domain.entity.LocalService;
import eu.renovaro.ad.domain.entity.ServiceType;
import eu.renovaro.user.domain.RoleName;
import eu.renovaro.user.domain.entity.Role;
import eu.renovaro.user.domain.entity.User;
import eu.renovaro.user.domain.entity.UserProfile;
import eu.renovaro.user.domain.entity.UserRole;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AdLocalSpecification {

    public static Specification<Ad> matches(AdFilterRequest request, Long subcategoryId) {
        return (root, query, cb) -> {
            query.orderBy(cb.desc(root.get("createdAt")));
            List<Predicate> predicates = new ArrayList<>();

            Join<Ad, ServiceType> serviceType = root.join("serviceType", JoinType.LEFT);
            Join<Ad, UserProfile> userProfile = root.join("userProfile", JoinType.LEFT);
            Join<UserProfile, User> user = userProfile.join("user", JoinType.LEFT);
            Join<Ad, LocalService> localService = root.join("localService", JoinType.LEFT);

            if (subcategoryId != null) {
                predicates.add(cb.equal(root.get("subcategory").get("subcategoryId"), subcategoryId));
            }

            predicates.add(cb.equal(root.get("adStatus").get("adStatusCode"), AdStatusCode.ACTIVE));
            predicates.add(serviceType.get("serviceTypeCode").in(ServiceTypeCode.LOCAL_FLEX));

            if (request != null) {

                if (request.getCityId() != null) {
                    predicates.add(cb.equal(root.get("city").get("cityId"), request.getCityId()));
                }

                if (request.getRating() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(
                            userProfile.get("ratingAverage"),
                            request.getRating().doubleValue()
                    ));
                }

                if (request.getPublishedBy() != null) {
                    RoleName selectedRole = null;

                    if (request.getPublishedBy() == ProviderRole.COMPANY) {
                        selectedRole = RoleName.COMPANY;
                    } else if (request.getPublishedBy() == ProviderRole.SPECIALIST) {
                        selectedRole = RoleName.SPECIALIST;
                    }

                    if (selectedRole != null) {
                        Subquery<Integer> existingRole = query.subquery(Integer.class);
                        Root<UserRole> ur = existingRole.from(UserRole.class);
                        Join<UserRole, Role> role = ur.join("role", JoinType.INNER);

                        existingRole.select(cb.literal(1));
                        existingRole.where(
                                cb.equal(ur.get("userId"), user.get("userId")),
                                cb.equal(role.get("roleName"), selectedRole)
                        );

                        predicates.add(cb.exists(existingRole));
                    }
                }

                Expression<BigDecimal> priceMin = localService.get("servicePriceMin");
                Expression<BigDecimal> priceForFilter = cb.coalesce(priceMin, new BigDecimal("999999999"));

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
            } else {
                if (!Long.class.equals(query.getResultType()) && !long.class.equals(query.getResultType())) {
                    query.orderBy(cb.desc(root.get("createdAt")), cb.desc(root.get("adId")));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

