package bg.sofia.uni.fmi.mjt.socialnetwork.post;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import bg.sofia.uni.fmi.mjt.socialnetwork.profile.DefaultUserProfile;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

public class SocialFeedPost implements Post{

private final UserProfile authorProfile;
private final String content;
private final Map<ReactionType, Set<UserProfile>> reactions;
private final LocalDateTime publishDate;
private  int reactionsCount = 0;
private static int id = 0;

public SocialFeedPost(UserProfile author, String content) {
       this.authorProfile =  new DefaultUserProfile(author.getUsername());
       this. content = content;
       this. reactions = new HashMap<>();
       this.publishDate = LocalDateTime.now();
       this.id++;
}

@Override
public boolean addReaction(UserProfile userProfile, ReactionType reactionType) {
  
    if (reactionType == null || userProfile == null) {
       throw new IllegalArgumentException();
   }

    // first if the user has already reacted we should remove this reaction
    boolean firstReaction = true;

    if(!reactions.containsKey(reactionType)){
        reactions.put(reactionType, new HashSet<>());
    }

 
    for (var pairsReactionUsersReacted : reactions.entrySet()) {
       
        if(pairsReactionUsersReacted.getValue().contains(userProfile)) {

            pairsReactionUsersReacted.getValue().remove(userProfile);
            if(pairsReactionUsersReacted.getValue().size() == 0){
                reactions.remove(reactionType);
            }
            firstReaction = false;
            reactionsCount--;
            break;
        }
    }

    reactions.get(reactionType).add(userProfile);
    reactionsCount++;
    return firstReaction;
}

@Override
public Map<ReactionType, Set<UserProfile>> getAllReactions() {
    return Collections.unmodifiableMap(reactions);
}

@Override
public UserProfile getAuthor() {
    return new DefaultUserProfile(authorProfile.getUsername());
}

@Override
public String getContent() {
   return content;
}

@Override
public LocalDateTime getPublishedOn() {
    LocalDateTime copy = publishDate;
    return copy;
}

@Override
public int getReactionCount(ReactionType reactionType) {
   
    if (reactionType == null) {
        throw new IllegalArgumentException();
    }
    
    if(reactions.containsKey(reactionType)) {
        return reactions.get(reactionType).size();
    }

    return 0;
}

@Override
public String getUniqueId() {
    return Integer.toString(id);
}

@Override
public boolean removeReaction(UserProfile userProfile) {
    
    if (userProfile == null) {
        throw new IllegalArgumentException("null input for user profile");
    }

     for (var pairsReactionUsersReacted : reactions.entrySet()) {
        
      if (pairsReactionUsersReacted.getValue().contains(userProfile)) {
             pairsReactionUsersReacted.getValue().remove(userProfile);
             reactionsCount--;
      
      if (pairsReactionUsersReacted.getValue().isEmpty()) {
        reactions.remove(pairsReactionUsersReacted.getKey());
      }
         return true;
    }
    }

    return false;
}

@Override
public int totalReactionsCount() {
    return reactionsCount;
}

@Override
public int hashCode() {
   return Objects.hash(id);
}

@Override
public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
        
    SocialFeedPost other = (SocialFeedPost) obj;
   return Objects.equals(id, other.id);
}



}
