import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.ArrayList;

public class SubscriptionGUI extends JFrame implements ActionListener
{
    


    private static final Color BG_DARK    = new Color(10,  14,  30);   // main window background //
    private static final Color BG_PANEL   = new Color(18,  24,  48);   // panel background //
    private static final Color BORDER_CLR = new Color(50,  70, 120);   // border lines  //
    private static final Color FG_TEXT    = new Color(210, 220, 245);  // label and text colour //
    private static final Color ACCENT     = new Color(90,  190, 255);  // section title colour //
    private static final Color BTN_GREEN  = new Color(34,  160,  90);  // confirm / submit actions //
    private static final Color BTN_RED    = new Color(200,  55,  55);  // destructive / remove actions //
    private static final Color BTN_BLUE   = new Color(45,  120, 230);  // primary add actions //
    private static final Color BTN_TEAL   = new Color(20,  170, 160);  // secondary actions //

    
    private static final Font FONT_LABEL  = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("SansSerif", Font.BOLD,  14);
    private static final Font FONT_TITLE  = new Font("SansSerif", Font.BOLD,  14);
    private static final Font FONT_MONO   = new Font("Monospaced", Font.PLAIN, 14);

  

    // List that holds every plan the user has created during this session //
    private ArrayList<AIModel> plans;
    private JTextField modelNameField, pricingField, parametersField, contextWindowField;
    private JTextField promptsRemainingField, promptTextField, responseLengthField, purchaseAmountField;
    private JTextField slotsField, memberNameField;

    //  Output //
    private JTextArea outputArea;

    //  Buttons //

    // Main action buttons sitting at the bottom of the window
    private JButton addPersonalBtn, addProBtn, displayAllBtn, clearBtn;

   
    private JButton enterPromptBtn, purchasePromptsBtn;

    // Buttons inside the Pro Plan card
    private JButton addMemberBtn, removeMemberBtn;

    

    // cardPanel holds the two plan cards; cardLayout switches between them
    private JPanel cardPanel;
    private CardLayout cardLayout;

    private static final String PERSONAL_CARD = "Personal Plan";
    private static final String PRO_CARD      = "Pro Plan";


   

    /** Creates the window, sets it up, and makes it visible. */
    public SubscriptionGUI()
    {
        super("AI Subscription Manager");
        plans = new ArrayList<AIModel>();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1150, 740);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        initComponents();
        setVisible(true);
    }


    // Layout Builder  //

    /** Assembles all four zone panels into the frame. */
    private void initComponents()
    {
        setLayout(new BorderLayout(14, 14));
        add(createInputPanel(),  BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
        add(createOutputPanel(), BorderLayout.EAST);
    }


    //  Factory Helpers //

    /** Returns a titled border with the shared accent colour and font. */
    private Border makeTitledBorder(String title)
    {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_CLR, 1),
            title, TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION, FONT_TITLE, ACCENT);
    }

    
    private JTextField makeField(int cols)
    {
        JTextField f = new JTextField(cols);
        f.setBackground(BG_DARK);
        f.setForeground(FG_TEXT);
        f.setCaretColor(ACCENT);
        f.setFont(FONT_LABEL);
        // Compound border //
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_CLR),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        return f;
    }

    /** Builds a styled label with the shared text colour and font. */
    private JLabel makeLabel(String text)
    {
        JLabel l = new JLabel(text);
        l.setForeground(FG_TEXT);
        l.setFont(FONT_LABEL);
        return l;
    }

    /** Builds a styled button with the given background colour. */
    private JButton makeButton(String text, Color bg)
    {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(FONT_BUTTON);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        b.addActionListener(this);
        return b;
    }


    //  Panel Builder //

    
    private JPanel createInputPanel()
    {
        JPanel panel = new JPanel(new GridLayout(5, 2, 8, 8));
        panel.setBorder(BorderFactory.createCompoundBorder(
            makeTitledBorder("  AI Model Details"),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        panel.setBackground(BG_PANEL);

        panel.add(makeLabel("  Field"));
        panel.add(makeLabel("  Value"));

        panel.add(makeLabel("  Model Name:"));
        panel.add(modelNameField = makeField(11));

        panel.add(makeLabel("  Pricing ($):"));
        panel.add(pricingField = makeField(11));

        panel.add(makeLabel("  Parameters (billions):"));
        panel.add(parametersField = makeField(11));

        panel.add(makeLabel("  Context Window (tokens):"));
        panel.add(contextWindowField = makeField(11));

        return panel;
    }

   
    private JPanel createCenterPanel()
    {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
            makeTitledBorder("  Plan Management"),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        centerPanel.setBackground(BG_PANEL);

        // Switcher row — FlowLayout keeps the label and combo box side by side
        JPanel switcherPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        switcherPanel.setBackground(BG_PANEL);
        switcherPanel.add(makeLabel("Select Plan Type: "));

        JComboBox<String> planSwitcher = new JComboBox<>(new String[]{ PERSONAL_CARD, PRO_CARD });
        planSwitcher.setBackground(BG_DARK);
        planSwitcher.setForeground(FG_TEXT);
        planSwitcher.setFont(FONT_LABEL);
        // When the selected item changes, flip to the matching card
        planSwitcher.addActionListener(e ->
            cardLayout.show(cardPanel, (String) planSwitcher.getSelectedItem()));
        switcherPanel.add(planSwitcher);

        // Card panel holds one card for each plan type
        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);
        cardPanel.setBackground(BG_PANEL);
        cardPanel.add(createPersonalCard(), PERSONAL_CARD);
        cardPanel.add(createProCard(),      PRO_CARD);

        centerPanel.add(switcherPanel);
        centerPanel.add(Box.createVerticalStrut(8));
        centerPanel.add(cardPanel);
        return centerPanel;
    }

    
    private JPanel createPersonalCard()
    {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(makeTitledBorder("  Personal Plan - Prompt Management"));
        panel.setBackground(BG_PANEL);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 18, 10, 18); // generous spacing around every cell
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(makeLabel("Prompts Remaining:"), gbc);
        gbc.gridx = 1; panel.add(promptsRemainingField = makeField(10), gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(makeLabel("Prompt Text:"), gbc);
        gbc.gridx = 1; panel.add(promptTextField = makeField(22), gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(makeLabel("Response Length (tokens):"), gbc);
        gbc.gridx = 1; panel.add(responseLengthField = makeField(10), gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(enterPromptBtn = makeButton("Enter Prompt", BTN_GREEN), gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(makeLabel("Purchase Prompts:"), gbc);
        gbc.gridx = 1; panel.add(purchaseAmountField = makeField(10), gbc);

        gbc.gridx = 1; gbc.gridy = 5;
        panel.add(purchasePromptsBtn = makeButton("Purchase Prompts", BTN_BLUE), gbc);

        return panel;
    }

   
    private JPanel createProCard()
    {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(makeTitledBorder("  Pro Plan - Team Collaboration"));
        panel.setBackground(BG_PANEL);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 18, 10, 18);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(makeLabel("Available Slots:"), gbc);
        gbc.gridx = 1; panel.add(slotsField = makeField(10), gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(makeLabel("Team Member Name:"), gbc);
        gbc.gridx = 1; panel.add(memberNameField = makeField(18), gbc);

        // Add and Remove buttons placed side by side for quick access
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(addMemberBtn = makeButton("Add Member", BTN_GREEN), gbc);
        gbc.gridx = 1;
        panel.add(removeMemberBtn = makeButton("Remove Member", BTN_RED), gbc);

        return panel;
    }

   
    private JPanel createButtonPanel()
    {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 12));
        panel.setBackground(BG_DARK);

        panel.add(addPersonalBtn = makeButton("Add Personal Plan", BTN_BLUE));
        panel.add(addProBtn      = makeButton("Add Pro Plan",      BTN_TEAL));
        panel.add(displayAllBtn  = makeButton("Display All",       BTN_GREEN));
        panel.add(clearBtn       = makeButton("Clear",             BTN_RED));

        return panel;
    }

   
    private JPanel createOutputPanel()
    {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createCompoundBorder(
            makeTitledBorder("  Output"),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        panel.setBackground(BG_PANEL);

        outputArea = new JTextArea(20, 38);
        outputArea.setEditable(false);
        outputArea.setFont(FONT_MONO);
        outputArea.setBackground(BG_DARK);
        outputArea.setForeground(FG_TEXT);
        outputArea.setCaretColor(ACCENT);
        outputArea.setLineWrap(true);     // wrap long lines so nothing is cut off
        outputArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.getViewport().setBackground(BG_DARK);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_CLR));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }


    //  ActionListener //

    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        if      (src == addPersonalBtn)     addPersonalPlan();
        else if (src == addProBtn)          addProPlan();
        else if (src == displayAllBtn)      displayAll();
        else if (src == clearBtn)           clearAll();
        else if (src == enterPromptBtn)     enterPromptAction();
        else if (src == purchasePromptsBtn) purchasePromptsAction();
        else if (src == addMemberBtn)       addTeamMemberAction();
        else if (src == removeMemberBtn)    removeTeamMemberAction();
    }


    //  Shared Helpers //

    
    private void showError(String msg)
    {
        JOptionPane.showMessageDialog(this, msg, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Reads and validates the four fields that every plan type needs.
     * Returns a String array {name, price, params, window},
     * or null if any field is empty or contains a value of the wrong type.
     */
    private String[] readCommonFields()
    {
        String name  = modelNameField.getText().trim();
        String price = pricingField.getText().trim();
        String prms  = parametersField.getText().trim();
        String win   = contextWindowField.getText().trim();

        if (name.isEmpty() || price.isEmpty() || prms.isEmpty() || win.isEmpty())
        {
            showError("Please fill in all Model Details fields.");
            return null;
        }

        try { Double.parseDouble(price); Integer.parseInt(prms); Integer.parseInt(win); }
        catch (NumberFormatException ex)
        {
            showError("Pricing must be a decimal (e.g. 9.99).\n"
                    + "Parameters and Context Window must be whole numbers.");
            return null;
        }

        return new String[]{ name, price, prms, win };
    }

    
    private PersonalPlan findLastPersonalPlan()
    {
        for (int i = plans.size() - 1; i >= 0; i--)
            if (plans.get(i) instanceof PersonalPlan)
                return (PersonalPlan) plans.get(i);
        return null;
    }

    
    private ProPlan findLastProPlan()
    {
        for (int i = plans.size() - 1; i >= 0; i--)
            if (plans.get(i) instanceof ProPlan)
                return (ProPlan) plans.get(i);
        return null;
    }


    // Button Handlers 

   
    private void addPersonalPlan()
    {
        String[] c = readCommonFields();
        if (c == null) return;

        String ps = promptsRemainingField.getText().trim();
        if (ps.isEmpty()) { showError("Please enter Prompts Remaining."); return; }

        int prompts;
        try { prompts = Integer.parseInt(ps); }
        catch (NumberFormatException ex)
        { showError("Prompts Remaining must be a whole number (e.g. 50)."); return; }

        PersonalPlan plan = new PersonalPlan(c[0], Double.parseDouble(c[1]),
                Integer.parseInt(c[2]), Integer.parseInt(c[3]), prompts);
        plans.add(plan);
        outputArea.setText("Personal Plan added! (Total plans: " + plans.size() + ")\n\n" + plan.display());
    }

    
    private void addProPlan()
    {
        String[] c = readCommonFields();
        if (c == null) return;

        String ss = slotsField.getText().trim();
        if (ss.isEmpty()) { showError("Please enter Available Slots."); return; }

        int slots;
        try { slots = Integer.parseInt(ss); }
        catch (NumberFormatException ex)
        { showError("Available Slots must be a whole number (e.g. 5)."); return; }

        ProPlan plan = new ProPlan(c[0], Double.parseDouble(c[1]),
                Integer.parseInt(c[2]), Integer.parseInt(c[3]), slots);
        plans.add(plan);
        outputArea.setText("Pro Plan added! (Total plans: " + plans.size() + ")\n\n" + plan.display());
    }

    
    private void displayAll()
    {
        if (plans.isEmpty()) { outputArea.setText("No plans yet — add one first."); return; }

        StringBuilder sb = new StringBuilder("=== All Plans (" + plans.size() + ") ===\n\n");
        for (int i = 0; i < plans.size(); i++)
            sb.append("--- Plan ").append(i + 1).append(" ---\n")
              .append(plans.get(i).display()).append("\n\n");

        outputArea.setText(sb.toString());
    }

    
    private void clearAll()
    {
        for (JTextField f : new JTextField[]{ modelNameField, pricingField, parametersField,
                contextWindowField, promptsRemainingField, promptTextField,
                responseLengthField, purchaseAmountField, slotsField, memberNameField })
            f.setText("");
        outputArea.setText("");
    }

    
    private void enterPromptAction()
    {
        PersonalPlan plan = findLastPersonalPlan();
        if (plan == null) { showError("No Personal Plan found. Add one first."); return; }

        String prompt = promptTextField.getText().trim();
        String ls     = responseLengthField.getText().trim();
        if (prompt.isEmpty() || ls.isEmpty())
        { showError("Please fill in Prompt Text and Response Length."); return; }

        int length;
        try { length = Integer.parseInt(ls); }
        catch (NumberFormatException ex)
        { showError("Response Length must be a whole number (e.g. 512)."); return; }

        outputArea.setText("Plan: " + plan.getModelName() + "\n\n" + plan.usePrompt(prompt, length));
    }

    
    private void purchasePromptsAction()
    {
        PersonalPlan plan = findLastPersonalPlan();
        if (plan == null) { showError("No Personal Plan found. Add one first."); return; }

        String as = purchaseAmountField.getText().trim();
        if (as.isEmpty()) { showError("Please enter how many prompts to purchase."); return; }

        int amount;
        try { amount = Integer.parseInt(as); }
        catch (NumberFormatException ex)
        { showError("Purchase amount must be a whole number (e.g. 100)."); return; }

        outputArea.setText("Plan: " + plan.getModelName() + "\n\n" + plan.purchasePrompts(amount));
    }

    
    private void addTeamMemberAction()
    {
        ProPlan plan = findLastProPlan();
        if (plan == null) { showError("No Pro Plan found. Add one first."); return; }

        String name = memberNameField.getText().trim();
        if (name.isEmpty()) { showError("Please enter the team member's name."); return; }

        outputArea.setText("Plan: " + plan.getModelName() + "\n\n" + plan.addTeamMember(name));
    }

    
    private void removeTeamMemberAction()
    {
        ProPlan plan = findLastProPlan();
        if (plan == null) { showError("No Pro Plan found. Add one first."); return; }

        String name = memberNameField.getText().trim();
        if (name.isEmpty()) { showError("Please enter the member's name to remove."); return; }

        outputArea.setText("Plan: " + plan.getModelName() + "\n\n" + plan.removeTeamMember(name));
    }


    //  Entry Point //

   
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new SubscriptionGUI());
    }
}