package at.htl.gca.model;

import javax.persistence.Entity;

@Entity
public class HobbyPlayer extends Golfer {
    private boolean isPremiumMember;

    public HobbyPlayer() {
    }

    public HobbyPlayer(String name, double hcp, int age, boolean isPremiumMember) {
        super(name, hcp, age);
        this.isPremiumMember = isPremiumMember;
    }

    public boolean isPremiumMember() {
        return isPremiumMember;
    }

    public void setPremiumMember(boolean premiumMember) {
        isPremiumMember = premiumMember;
    }
}
