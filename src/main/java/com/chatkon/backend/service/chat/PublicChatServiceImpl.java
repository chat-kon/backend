package com.chatkon.backend.service.chat;

import com.chatkon.backend.exception.BadRequestException;
import com.chatkon.backend.exception.ChatNotFoundException;
import com.chatkon.backend.exception.UserNotFoundException;
import com.chatkon.backend.model.entity.chat.*;
import com.chatkon.backend.model.entity.user.User;
import com.chatkon.backend.repository.AdminRepository;
import com.chatkon.backend.repository.MemberRepository;
import com.chatkon.backend.repository.PublicChatRepository;
import com.chatkon.backend.repository.UserRepository;
import com.chatkon.backend.util.LinkGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PublicChatServiceImpl implements PublicChatService {

    private final UserRepository userRepository;
    private final PublicChatRepository publicChatRepository;
    private final AdminRepository adminRepository;
    private final MemberRepository memberRepository;

    @Override
    public PublicChat createPublicChat(Long creatorId, PublicChat chat, Set<Long> initMemberIds) {
        User creator = findUser(creatorId);
        chat.setOwner(creator);
        chat.setLink(LinkGenerator.generate(20));
        chat.setCreatedAt(System.currentTimeMillis());
        PublicChat savedChat = publicChatRepository.save(chat);

        // todo
        addMembers(savedChat.getId(), creatorId, Set.of(creatorId));
        addMembers(savedChat.getId(), creatorId, initMemberIds);

        return savedChat;
    }

    @Override
    public PublicChat joinPublicChat(Long chatId, Long userId) {
        PublicChat chat = findPublicChat(chatId);
        User user = findUser(userId);

        // todo check user is not banned
        // todo other required checks

        saveMember(chat, user);

        return publicChatRepository.save(chat);
    }

    @Override
    public void leavePublicChat(Long chatId, Long userId) {
        PublicChat chat = findPublicChat(chatId);

        if (!isMember(chatId, userId)) {
            throw new BadRequestException();
        }

        deleteMember(chatId, userId);

        if (isAdmin(chatId, userId)) {
            deleteAdmin(chatId, userId);
        }

        if(chat.getOwner().getId().equals(userId)) {
            deleteChatWithMessages(chatId);
        }
    }

    private void deleteChatWithMessages(Long chatId) {
        memberRepository.deleteByChatId(chatId);
        publicChatRepository.deleteById(chatId);
    }

    private boolean isAdmin(Long chatId, Long userId) {
        AdminId adminId = createAdminId(chatId, userId);
        return adminRepository.existsById(adminId);
    }

    private boolean isMember(Long chatId, Long userId) {
        MemberId memberId = createMemberId(chatId, userId);
        return memberRepository.existsById(memberId);
    }

    @Override
    public Set<User> addMembers(Long chatId, Long adderId, Set<Long> userIds) {
        PublicChat chat = findPublicChat(chatId);
        User adder = findUser(adderId);

        boolean isAdmin = isAdmin(chatId, adderId);

        boolean isOwner = chat.getOwner().equals(adder);

        if (!isOwner && !isAdmin)
            throw new BadRequestException();

        Set<User> addedUsers = new HashSet<>();

        userIds.forEach(userId -> userRepository.findById(userId).ifPresent(user -> {
            Member addedMember = saveMember(chat, user);
            addedUsers.add(addedMember.getUser());
        }));

        return addedUsers;
    }

    private Member saveMember(PublicChat chat, User user) {

        MemberId memberId = MemberId.builder()
                .chatId(chat.getId())
                .userId(user.getId())
                .build();

        Member member = Member.builder()
                .id(memberId)
                .chat(chat)
                .user(user)
                .build();

        return memberRepository.save(member);
    }

    private Admin saveAdmin(PublicChat chat, User user) {

        AdminId adminId = AdminId.builder()
                .chatId(chat.getId())
                .userId(user.getId())
                .build();

        Admin admin = Admin.builder()
                .id(adminId)
                .chat(chat)
                .user(user)
                .build();

        return adminRepository.save(admin);
    }

    private void deleteAdmin(Long chatId, Long userId) {
        AdminId adminId = createAdminId(chatId, userId);
        adminRepository.deleteById(adminId);
    }

    private void deleteMember(Long chatId, Long userId) {
        MemberId memberId = createMemberId(chatId, userId);
        memberRepository.deleteById(memberId);
    }

    @Override
    public PublicChat deleteMember(Long chatId, Long deleterId, Long userId) {
        PublicChat chat = findPublicChat(chatId);
        User user = findUser(userId);
        User deleter = findUser(deleterId);

        if (chat.getOwner().equals(user))
            throw new BadRequestException();

        boolean isDeleterOwner = chat.getOwner().equals(deleter);
        boolean isDeleterAdmin = isAdmin(chatId, deleterId);

        if (!isDeleterOwner && !isDeleterAdmin)
            throw new BadRequestException();

        boolean isUserAdmin = isAdmin(chatId, userId);

        if (!isDeleterOwner && isUserAdmin)
            throw new BadRequestException();

        if(isUserAdmin) {
            deleteAdmin(chatId, userId);
        }

        deleteMember(chatId, userId);

        return publicChatRepository.save(chat);
    }

    @Override
    public Admin addAdmin(Long chatId, Long adderId, Long userId) {
        PublicChat chat = findPublicChat(chatId);
        User user = findUser(userId);
        User adder = findUser(adderId);

        boolean isUserMember = isMember(chatId, userId);
        boolean isUserAdmin = isAdmin(chatId, userId);
        boolean isUserOwner = chat.getOwner().equals(user);

        if (!chat.getOwner().equals(adder))
            throw new BadRequestException();

        if (!isUserMember)
            throw new BadRequestException();

        if (isUserOwner || isUserAdmin)
            throw new BadRequestException();

        return saveAdmin(chat, user);
    }

    @Override
    public PublicChat dismissAdmin(Long chatId, Long deleterId, Long userId) {
        PublicChat chat = findPublicChat(chatId);
        User admin = findUser(userId);
        User selector = findUser(deleterId);

        if (!chat.getOwner().equals(selector))
            throw new BadRequestException();

        if (chat.getOwner().equals(admin))
            throw new BadRequestException();

        boolean isUserAdmin = isAdmin(chatId, userId);

        if (!isUserAdmin)
            throw new BadRequestException();

        deleteAdmin(chatId, userId);

        return publicChatRepository.save(chat);
    }

    @Override
    public PublicChat editProfilePublicChat(PublicChat publicChat, Long editorId) {
        User editor = findUser(editorId);
        PublicChat chat = findPublicChat(publicChat.getId());

        boolean isAdmin = isAdmin(publicChat.getId(), editorId);
        boolean isOwner = chat.getOwner().equals(editor);

        if (!isAdmin && !isOwner) {
            throw new BadRequestException();
        }

        chat.setAvatar(publicChat.getAvatar());
        chat.setTitle(publicChat.getTitle());

        return publicChatRepository.save(chat);
    }

    @Override
    public Set<User> getChatMembers(Long chatId) {
        return memberRepository.findMembersByChatId(chatId).stream()
                .map(Member::getUser)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getChatAdmins(Long chatId) {
        return adminRepository.findAdminsByIdChatId(chatId).stream()
                .map(Admin::getUser)
                .collect(Collectors.toSet());
    }

    @Override
    public PublicChat findPublicChat(Long chatId) {
        return publicChatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
    }

    @Override
    public Member findMember(Long chatId, Long userId) {
        MemberId memberId = createMemberId(chatId, userId);
        return memberRepository.findById(memberId).orElseThrow(UserNotFoundException::new);
    }

    private AdminId createAdminId(Long chatId, Long userId) {
        return AdminId.builder().chatId(chatId).userId(userId).build();
    }

    private MemberId createMemberId(Long chatId, Long userId) {
        return MemberId.builder().chatId(chatId).userId(userId).build();
    }

    @Override
    public Admin findAdmin(Long chatId, Long userId) {
        AdminId adminId = createAdminId(chatId, userId);
        return adminRepository.findById(adminId).orElseThrow(UserNotFoundException::new);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Set<User> usersCanBeAdd(Long chatId, Set<Long> userIds) {
        return userIds.stream().map(this::findUser)
                .filter(User::getAllowedInvite)
                .collect(Collectors.toSet());
    }
}
