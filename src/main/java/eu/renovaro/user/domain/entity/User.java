package eu.renovaro.user.domain.entity;

import eu.renovaro.chat.domain.entity.ChatUser;
import eu.renovaro.chat.domain.entity.Message;
import eu.renovaro.common.util.EncryptedStringConverter;
import eu.renovaro.notification.domain.entity.Notification;
import eu.renovaro.notification.domain.entity.NotificationObject;
import eu.renovaro.reservation.domain.entity.ServiceReservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Convert(converter = EncryptedStringConverter.class)
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Column(name = "email_address", unique = true)
    private String email;

    @NotNull
    @Column(name = "password")
    private String password;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "profile_image_id")
    private String profileImageId;

    @NotNull
    @Column(name = "joined_on")
    private LocalDateTime joinedOn;

    @ToString.Exclude
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<UserRole> userRoles = new HashSet<>();

    @OneToOne(mappedBy = "user")
    private UserProfile userProfile;

    @OneToMany(mappedBy = "user")
    private List<ServiceReservation> serviceBookings;

    @OneToMany(mappedBy = "recipientUser")
    private List<Notification> receivedNotifications;

    @OneToMany(mappedBy = "createdByUser")
    private List<NotificationObject> sentNotifications;

    @OneToMany(mappedBy = "user")
    private List<ChatUser> chatUserList;

    @OneToMany(mappedBy = "user")
    private List<Message> messageList;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Address> addresses;

}
