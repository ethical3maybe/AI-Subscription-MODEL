
public class ProPlan extends AIModel
{
    //                    Attributes              //

    // Tracks how many team member slots are still free on this plan
    private int availableSlots;


    //                   Constructor                 //

    /**
     * Creates a new ProPlan by forwarding the shared model details
     * to the AIModel constructor, then recording the number of
     * team slots this plan starts with.
     */
    public ProPlan(String modelName, double price, int parameterCount,
                   int windowSize, int availableSlots)
    {
        // Pass the four shared fields up to the parent class //
        super(modelName, price, parameterCount, windowSize);

        // Set the starting number of available team slots
        this.availableSlots = availableSlots;
    }


    //                 Getter Methods               //

    /**
     * Returns the number of team slots that are still free
     * and available for new members to fill.
     */
    public int getAvailableSlots()
    {
        return availableSlots;
    }


    //                       Team Management               //

    /**
     * Attempts to add a new member to the team.
     * If at least one slot is free, the slot count drops by one
     * and a success message is returned.
     * If all slots are already taken, an error message is returned instead.
     */
    public String addTeamMember(String memberName)
    {
        // Only proceed if there is at least one open slot //
        if (availableSlots > 0)
        {
            availableSlots--; // One slot is now occupied //

            return "Team member '" + memberName + "' added successfully. "
                    + "Remaining slots: " + availableSlots;
        }
        else
        {
            // All slots are full — cannot add another member //
            return "Error: No available slots. Cannot add team member '" + memberName + "'.";
        }
    }

    /**
     * Removes an existing member from the team, freeing up their slot.
     * The slot count increases by one to reflect the vacancy.
     */
    public String removeTeamMember(String memberName)
    {
        availableSlots++; // Slot becomes free again when a member leaves //

        return "Team member '" + memberName + "' removed. "
                + "Available slots: " + availableSlots;
    }


    //                 Display                   //
    
    @Override
    public String display()
    {
        return "Model Name: "       + getModelName()      +
               "\nPrice: "          + getPrice()           +
               "\nParameter Count: "+ getParameterCount()  +
               "\nContext Window: " + getWindowSize()      +
               "\nPlan Type: Pro Plan"                     +
               "\nAvailable Slots: "+ availableSlots;
    }
}