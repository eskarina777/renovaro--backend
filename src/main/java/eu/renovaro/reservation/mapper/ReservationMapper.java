package eu.renovaro.reservation.mapper;



import org.mapstruct.*;


@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface ReservationMapper {

}
