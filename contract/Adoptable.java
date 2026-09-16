package contract;

public interface Adoptable {
    boolean isAdoptionEligible();
    void completeAdoption();
}