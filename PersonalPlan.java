
public class PersonalPlan extends AIModel
{
    //                      Attributes             //

    private int availableTokens; // How many tokens the user currently has left //
    private int tokensUsed;      // Running total of tokens spent //


    //                       Constructor           //

    /**
     * Creates a new PersonalPlan by passing the shared model fields
     * up to the AIModel constructor, then setting the starting token balance.
     * The tokensUsed counter always begins at zero.
     */
    public PersonalPlan(String modelName, double price, int parameterCount,
                        int contextWindow, int availableTokens)
    {
        super(modelName, price, parameterCount, contextWindow);
        this.availableTokens = availableTokens; // Set the opening balance //
        this.tokensUsed      = 0;               // No tokens have been used //
    }


    //                    Getter Methods                    //  

    // Returns how many tokens the user still has available
    public int getAvailableTokens()
    {
        return availableTokens;
    }

    // Returns the total number of tokens spent so far
    public int getTokensUsed()
    {
        return tokensUsed;
    }


    // ---------------------- Token Management ----------------------

    /**
     * Adds more tokens to the user's balance.
     * The amount must be a positive whole number — purchasing
     * zero or a negative amount is not allowed.
     */
    public String purchaseTokens(int additionalTokens)
    {
        // Reject the request if the number of tokens to buy is not positive
        if (additionalTokens <= 0) {
            return "Error: Must purchase a positive number of tokens.";
        }

        availableTokens += additionalTokens; // Increase the available balance

        return "Successfully purchased " + additionalTokens +
               " tokens. Available tokens: " + availableTokens;
    }

    /**
     * A shortcut method that calls purchaseTokens() directly.
     * Provided so the GUI can refer to this action as "Purchase Prompts"
     * without needing to know the internal method name.
     */
    public String purchasePrompts(int additionalPrompts)
    {
        return purchaseTokens(additionalPrompts);
    }


    // ---------------------- Prompt Execution ----------------------

    /**
     * Submits a prompt to the model and deducts the token cost.
     * First checks whether there are enough tokens to cover the request.
     * If the prompt text plus output tokens plus overhead would exceed
     * the context window, calculateTokenUsage throws an exception which
     * is caught here and returned as a readable error message.
     */
    public String usePrompt(String promptText, int outputTokens)
    {
        try {
            int totalTokens = calculateTokenUsage(promptText, outputTokens);

            // Check the user has enough tokens before going ahead
            if (availableTokens < totalTokens) {
                return "Error: Insufficient available tokens.";
            }

            availableTokens -= totalTokens; // Deduct the cost from the balance
            tokensUsed      += totalTokens; // Add to the lifetime usage counter

            return "Prompt executed successfully. Tokens used: " + totalTokens +
                   ". Available tokens: " + availableTokens;

        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }


    // ---------------------- Display ----------------------

    /**
     * Builds and returns a formatted summary of this plan,
     * including all shared model details and the current token figures.
     */
    @Override
    public String display()
    {
        return "Model Name: "      + getModelName()      +
               "\nPrice: "         + getPrice()           +
               "\nParameter Count: "+ getParameterCount() +
               "\nContext Window: " + getWindowSize()     +
               "\nPlan Type: Personal Plan"               +
               "\nAvailable Tokens: " + availableTokens   +
               "\nTokens Used: "    + tokensUsed;
    }
}