
public abstract class AIModel
{
    // ---------------------- Attributes ----------------------

    private String modelName;      // Holds the name given to this AI model
    private double price;          // Holds the monthly cost of this model
    private int parameterCount;    // Holds how many parameters the model has
    private int windowSize;        // Holds the maximum number of tokens per request


    // ---------------------- Constructor ----------------------

    /**
     * Sets up a new AIModel using the four values passed in.
     * Every subclass calls this constructor via super() to
     * ensure the shared fields are always properly initialised.
     */
    public AIModel(String model1, double price1, int parameter1, int window1)
    {
        this.modelName      = model1;      // Store the model name //
        this.price          = price1;      // '' ''' '' price value //
        this.parameterCount = parameter1;  // '' ' '' ''parameter count //
        this.windowSize     = window1;     // '' ' '' '' context window size //
    }


    //                   Getter Methods                 // 

    // Returns the name of this AI model
    public String getModelName()
    {
        return this.modelName;
    }

    // Returns the price associated with this model
    public double getPrice()
    {
        return this.price;
    }

    // Returns the total number of parameters in this model
    public int getParameterCount()
    {
        return this.parameterCount;
    }

    // Returns the context window size measured in tokens
    public int getWindowSize()
    {
        return this.windowSize;
    }

  
    public int calculateTokenUsage(String promptText, int outputTokens)
    {
        int inputTokens  = promptText.trim().split("\\s+").length; // count words as a rough token estimate //
        int systemTokens = 100;                                     // fixed overhead added to every request //

        int totalTokens = inputTokens + outputTokens + systemTokens;

        //  if the total would exceed what this model can handle //
        if (totalTokens > windowSize) {
            throw new IllegalArgumentException("Token usage exceeds context window limit");
        }

        return totalTokens;
    }


    //                     Display Method             //

    /**
     * Each subclass must provide its own version of this method
     * so it can show plan-specific details alongside the shared model info.
     */
    public abstract String display();
}