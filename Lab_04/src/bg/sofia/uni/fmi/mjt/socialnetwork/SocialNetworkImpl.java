package bg.sofia.uni.fmi.mjt.socialnetwork;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.SocialFeedPost;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.Interest;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

public class SocialNetworkImpl implements SocialNetwork {

    private final Set<UserProfile> users;
    private final Set<Post> posts;

     public SocialNetworkImpl(){
         users = new HashSet<>();
         posts = new HashSet<>();
     }

    @Override
    public SortedSet<UserProfile> getAllProfilesSortedByFriendsCount() {
       return new TreeSet<>(users);
    }

    @Override
    public Set<UserProfile> getAllUsers() {
        return Collections.unmodifiableSet(users);
    }

    private boolean isRegistered(UserProfile user) {
        return users.contains(user);
    }

    @Override
    public Set<UserProfile> getMutualFriends(UserProfile userProfile1, UserProfile userProfile2)
            throws UserRegistrationException {
        
         if (userProfile1 == null || userProfile2 == null) {
            throw new IllegalArgumentException("The user profiles should not be null.");
         }       

         if (!isRegistered(userProfile1) || !(isRegistered(userProfile2))) {
            throw new UserRegistrationException("One or both of the users is not registered.");
         }

         Set<UserProfile> mutualFriends = new HashSet<>(userProfile1.getFriends());
         mutualFriends.retainAll(userProfile2.getFriends());
         return mutualFriends;
    }

    @Override
    public Collection<Post> getPosts() {
        return Collections.unmodifiableSet(posts);
    }

    private boolean hasCommonInterest(UserProfile userToCheck, UserProfile author) {
           
       Set<Interest>commonInterests  = new HashSet<>(userToCheck.getInterests());
       commonInterests.retainAll(author.getInterests());
       return commonInterests.size() == 0;
    }

    private boolean areMutualFriends(UserProfile userToCheck, UserProfile author, Set<UserProfile> visited) {

        if ( userToCheck.getFriends().size() == 0) {
            return false;
        }
         
        if (!visited.add(userToCheck)) {
            return false; 
        }


        for (var friendOfUser : userToCheck.getFriends()) {
            if(friendOfUser.isFriend(author) || areMutualFriends(friendOfUser, author,visited)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Set<UserProfile> getReachedUsers(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("The post is null.");
        }

        Set<UserProfile> reachedAudience = new HashSet<>();

        for( var user : users) {
            if (hasCommonInterest(user, post.getAuthor()) || areMutualFriends(user, post.getAuthor(),new HashSet<>())) {
                reachedAudience.add(user);
            }
        }
        return reachedAudience;
    }

    @Override
    public Post post(UserProfile userProfile, String content) throws UserRegistrationException {
        
        if (userProfile == null || content == null || content.isBlank()) {
            throw new IllegalArgumentException("Invalid argument for user profile or content.");
        }

        if(!isRegistered(userProfile)) {
            throw new UserRegistrationException("The user is not registered yet.");
        }

        Post newPost = new SocialFeedPost(userProfile, content);
        posts.add(newPost);
        return newPost;
    }

    @Override
    public void registerUser(UserProfile userProfile) throws UserRegistrationException {
       
        if(userProfile == null) {
            throw new IllegalArgumentException("The user profile is null.);
        }

       if(isRegistered(userProfile)) {
            throw new UserRegistrationException("The user is already registered.");
       }
 
       users.add(userProfile);
    }

}
