package bg.sofia.uni.fmi.mjt.socialnetwork.profile;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class DefaultUserProfile  implements UserProfile, Comparable<UserProfile>{

    private final String username;
    private final Set<Interest> interests;
    private final Set<UserProfile> friends;


    public DefaultUserProfile(String username) {
        this.username = UUID.randomUUID().toString();
        interests = new HashSet<>();
        friends = new HashSet<>();
    }

    @Override
    public boolean addFriend(UserProfile userProfile) {
      
        if(userProfile == null || userProfile.equals(this) ) {
            throw new IllegalArgumentException();
        }

        if (friends.contains(userProfile)){
            return false;
        }

        friends.add(userProfile);
        userProfile.addFriend(this);
        return true;
    }

    @Override
    public boolean addInterest(Interest interest) {
       
        if(interest == null) {
            throw new IllegalArgumentException();
        }
 
        return interests.add(interest);
    }

    @Override
    public Collection<UserProfile> getFriends() {
         return Collections.unmodifiableSet(new HashSet<UserProfile>(friends));
    }

    @Override
    public Collection<Interest> getInterests() {
         return Collections.unmodifiableSet(new HashSet<Interest>(interests));
    }

    @Override
    public String getUsername() {
        return username.toString();
    }

    @Override
    public boolean isFriend(UserProfile userProfile) {
      
        if(userProfile == null) {
            throw new IllegalArgumentException();
        }

        return friends.contains(userProfile);
    }

    @Override
    public boolean removeInterest(Interest interest) {
        
        if(interest == null) {
            throw new IllegalArgumentException();
        }
 
        return interests.remove(interest);
    }

    @Override
    public boolean unfriend(UserProfile userProfile) {
     
        if(userProfile == null) {
            throw new IllegalArgumentException();
        }

       if(!friends.contains(userProfile)){
        return false;
       }

       friends.remove(userProfile);
       userProfile.unfriend(this);
       return true;
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof DefaultUserProfile other)) {
            return false;
        }
            
        return this.username.equals(other.getUsername());
    }


    @Override
    public int compareTo(UserProfile o) {
        
        int countFriendsComparison = Integer.compare(o.getFriends().size(),getFriends().size());
        if( countFriendsComparison != 0){
            return countFriendsComparison;
        }
        return getUsername().compareTo(o.getUsername());
    }


  
}
