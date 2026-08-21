package matrix.evolution;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalEvolutionApp extends JFrame {

    // Model class representing evolutionary stage
    static class EvolutionStage {
        String era;
        String stageName;
        String phylumOrGroup;
        String keyInnovations;
        String description;

        public EvolutionStage(String era, String stageName, String phylumOrGroup, String keyInnovations, String description) {
            this.era = era;
            this.stageName = stageName;
            this.phylumOrGroup = phylumOrGroup;
            this.keyInnovations = keyInnovations;
            this.description = description;
        }

        @Override
        public String toString() {
            return stageName + " (" + era + ")";
        }
    }

    private List<EvolutionStage> stages;
    private JList<EvolutionStage> stageList;
    private JLabel titleLabel;
    private JLabel eraLabel;
    private JLabel phylumLabel;
    private JTextArea innovationsArea;
    private JTextArea descriptionArea;

    public AnimalEvolutionApp() {
        setTitle("Zoology: Evolutionary Lineage of Animals");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initData();
        initComponents();
    }

    private void initData() {
        stages = new ArrayList<>();
        stages.add(new EvolutionStage(
            "Precambrian (~1.5–1.0 Mya)",
            "Single-Celled Eukaryotes",
            "Protista / Choanoflagellata",
            "Membrane-bound organelles, true nucleus, flagella",
            "Ancestral single-celled eukaryotic protists. Choanoflagellates form colonies and represent the closest living unicellular relatives of animals."
        ));
        stages.add(new EvolutionStage(
            "Precambrian (~600 Mya)",
            "Multicellularity & Specialized Cells",
            "Porifera (Sponges)",
            "Multicellularity, cellular division of labor, lack of true tissues",
            "Aggregation of eukaryotic cells leading to parazoan organisms. Sponges possess specialized cells like choanocytes but lack true tissue layers."
        ));
        stages.add(new EvolutionStage(
            "Ediacaran (~580 Mya)",
            "Diploblastic & Radial Symmetry",
            "Cnidaria (Jellyfish, Corals)",
            "True tissues (Ectoderm, Endoderm), radial symmetry, nerve net",
            "Eumetazoans emerge with two germ layers (diploblastic) and simple gastrovascular cavities. Development of primitive nervous systems."
        ));
        stages.add(new EvolutionStage(
            "Cambrian (~540 Mya)",
            "Triploblastic & Bilateral Symmetry",
            "Platyhelminthes / Protostomes",
            "Bilateral symmetry, Cephalization, Mesoderm layer",
            "Bilaterians develop a third germ layer (mesoderm) enabling complex organ formation. Cephalization (head/brain concentration) begins."
        ));
        stages.add(new EvolutionStage(
            "Cambrian Explosion (~530 Mya)",
            "Coelom & Body Cavities",
            "Annelida, Mollusca, Arthropoda",
            "True coelom, segmentation, hard exoskeletons, compound eyes",
            "Rapid diversification of coelomate invertebrates. Development of fluid-filled body cavities and specialized jointed appendages."
        ));
        stages.add(new EvolutionStage(
            "Cambrian / Ordovician (~500 Mya)",
            "Deuterostomes & Notochord",
            "Early Chordates (e.g., Pikaia, Agnatha)",
            "Notochord, dorsal hollow nerve cord, pharyngeal slits, post-anal tail",
            "Transition to chordate architecture. Early jawless fish emerge as the foundation for modern vertebrate lineages."
        ));
        stages.add(new EvolutionStage(
            "Devonian (~375 Mya)",
            "Tetrapod Transition to Land",
            "Amphibia (e.g., Tiktaalik ancestor)",
            "Lungs, tetrapod limb structures, pectoral girdle shift",
            "Lobe-finned fishes adapt to terrestrial habitats, giving rise to amphibians capable of breathing air while relying on water for reproduction."
        ));
        stages.add(new EvolutionStage(
            "Carboniferous / Jurassic (~310–150 Mya)",
            "Amniotes, Birds & Mammals",
            "Reptilia, Aves, Mammalia",
            "Amniotic egg, endothermy, hair/feathers, 4-chambered heart",
            "Evolution of the amniotic egg frees vertebrates from water dependence. Mammals and modern avian lineages undergo adaptive radiation."
        ));
    }

    private void initComponents() {
        // Main Container
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Header
        JLabel header = new JLabel("Evolutionary Lineage: Unicellular Eukaryote to Complex Vertebrates", JLabel.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        mainPanel.add(header, BorderLayout.NORTH);

        // Left Panel: Timeline Selection
        stageList = new JList<>(stages.toArray(new EvolutionStage[0]));
        stageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        stageList.setSelectedIndex(0);
        stageList.setFont(new Font("SansSerif", Font.PLAIN, 13));
        
        JScrollPane listScrollPane = new JScrollPane(stageList);
        listScrollPane.setPreferredSize(new Dimension(280, 0));
        listScrollPane.setBorder(BorderFactory.createTitledBorder("Evolutionary Stages"));
        mainPanel.add(listScrollPane, BorderLayout.WEST);

        // Right Panel: Details
        JPanel detailPanel = new JPanel(new GridBagLayout());
        detailPanel.setBorder(BorderFactory.createTitledBorder("Biological Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        // Stage Title
        titleLabel = new JLabel();
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 80, 150));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        detailPanel.add(titleLabel, gbc);

        // Era
        gbc.gridy = 1; gbc.gridwidth = 1;
        detailPanel.add(new JLabel("Timeline / Era:"), gbc);
        eraLabel = new JLabel();
        eraLabel.setFont(new Font("SansSerif", Font.ITALIC, 13));
        gbc.gridx = 1;
        detailPanel.add(eraLabel, gbc);

        // Phylum/Taxa
        gbc.gridx = 0; gbc.gridy = 2;
        detailPanel.add(new JLabel("Taxonomic Group:"), gbc);
        phylumLabel = new JLabel();
        phylumLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridx = 1;
        detailPanel.add(phylumLabel, gbc);

        // Key Innovations
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        detailPanel.add(new JLabel("Key Evolutionary Innovations:"), gbc);
        
        innovationsArea = new JTextArea(3, 30);
        innovationsArea.setEditable(false);
        innovationsArea.setLineWrap(true);
        innovationsArea.setWrapStyleWord(true);
        innovationsArea.setBackground(new Color(245, 245, 245));
        gbc.gridy = 4;
        detailPanel.add(new JScrollPane(innovationsArea), gbc);

        // Overview / Description
        gbc.gridy = 5;
        detailPanel.add(new JLabel("Description & Evolutionary Context:"), gbc);

        descriptionArea = new JTextArea(5, 30);
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        gbc.gridy = 6; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        detailPanel.add(new JScrollPane(descriptionArea), gbc);

        mainPanel.add(detailPanel, BorderLayout.CENTER);

        // Selection Listener
        stageList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    updateDetails(stageList.getSelectedValue());
                }
            }
        });

        // Initialize first view
        updateDetails(stages.get(0));

        add(mainPanel);
    }

    private void updateDetails(EvolutionStage stage) {
        if (stage != null) {
            titleLabel.setText(stage.stageName);
            eraLabel.setText(stage.era);
            phylumLabel.setText(stage.phylumOrGroup);
            innovationsArea.setText(stage.keyInnovations);
            descriptionArea.setText(stage.description);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AnimalEvolutionApp().setVisible(true);
        });
    }
}