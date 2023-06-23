package com.chatkon.backend.service.user;

import com.chatkon.backend.exception.InvalidPasswordException;
import com.chatkon.backend.exception.UserNotFoundException;
import com.chatkon.backend.exception.UsernameAlreadyTakenException;
import com.chatkon.backend.model.entity.chat.Chat;
import com.chatkon.backend.model.entity.chat.Member;
import com.chatkon.backend.model.entity.user.User;
import com.chatkon.backend.repository.MemberRepository;
import com.chatkon.backend.repository.PrivateChatRepository;
import com.chatkon.backend.repository.PublicChatRepository;
import com.chatkon.backend.repository.UserRepository;
import com.chatkon.backend.util.PasswordChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PrivateChatRepository privateChatRepository;
    private final PublicChatRepository publicChatRepository;
    private final MemberRepository memberRepository;

    @Override
    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User saveUser(User user) {
        checkUniqueUsername(user.getUsername());
//        checkStrongPassword(user.getPassword());
        hashPassword(user);
        user.setAllowedInvite(true);
        user.setVisibleAvatar(true);
        return userRepository.save(user);
    }

    private void hashPassword(User user) {
        
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Set<Chat> getUserChats(Long userId) {
        Set<Member> members = memberRepository.findMembersByUserId(userId);
        Set<Chat> chats = members.stream().map(Member::getChat).collect(Collectors.toSet());
        chats.addAll(privateChatRepository.findPrivateChatsByUser1Id(userId));
        chats.addAll(privateChatRepository.findPrivateChatsByUser2Id(userId));
        return chats;
    }

    @Override
    public User findUser(String username, String password) {
        return userRepository.findUserByUsernameAndPassword(username, password)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User findUser(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User editProfile(User userInput, Long userId) {
        User user = findUser(userId);
        user.setId(userInput.getId());
        user.setName(userInput.getName());
        user.setAvatar(userInput.getAvatar());
        user.setUsername(userInput.getUsername());
        user.setEmail(userInput.getEmail());
        user.setAllowedInvite(user.getAllowedInvite());
        user.setVisibleAvatar(user.getVisibleAvatar());
        return userRepository.save(user);
    }

    private void checkUniqueUsername(String username) {
        boolean userExist = userRepository.findUserByUsername(username).isPresent();
        if (userExist) {
            throw new UsernameAlreadyTakenException();
        }
    }

    private void checkStrongPassword(String password) {
        if (!PasswordChecker.isStrong(password)) {
            throw new InvalidPasswordException();
        }
    }
}