package matrix.evolution;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class AnimalEvolutionFromReptilestoHomoSapiensApp extends JFrame {

    // Evolutionary stage data model
    static class EvolutionaryStage {
        String stageName;
        String period;
        String anatomicalFeatures;
        String evolutionarySignificance;

        public EvolutionaryStage(String stageName, String period, String anatomicalFeatures, String evolutionarySignificance) {
            this.stageName = stageName;
            this.period = period;
            this.anatomicalFeatures = anatomicalFeatures;
            this.evolutionarySignificance = evolutionarySignificance;
        }

        @Override
        public String toString() {
            return stageName;
        }
    }

    private JList<EvolutionaryStage> stageList;
    private JLabel titleLabel;
    private JLabel periodLabel;
    private JTextArea featuresArea;
    private JTextArea significanceArea;

    public AnimalEvolutionFromReptilestoHomoSapiensApp() {
        setTitle("Zoology: Lineage from Reptiles to Homo Sapiens");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Populate evolutionary stages (Synapsid lineage)
        DefaultListModel<EvolutionaryStage> listModel = new DefaultListModel<>();
        listModel.addElement(new EvolutionaryStage(
                "1. Stem Amniotes / Early Reptiles",
                "Carboniferous (~312 Mya)",
                "Amniotic egg, scaly keratinized skin, sprawling limb posture.",
                "Independence from aquatic environments for reproduction; base of sauropsid and synapsid lineages."
        ));
        listModel.addElement(new EvolutionaryStage(
                "2. Pelycosaurs (e.g., Dimetrodon)",
                "Early Permian (~295 Mya)",
                "Single temporal fenestra behind eye orbit (synapsid skull structure), differentiated teeth.",
                "First major synapsid divergence separating mammal ancestors from traditional reptiles."
        ));
        listModel.addElement(new EvolutionaryStage(
                "3. Therapsids (e.g., Inostrancevia)",
                "Middle to Late Permian (~275 Mya)",
                "More erect limb posture under body, enlarged dentary bone, specialized incisors and canines.",
                "Transition toward higher metabolic rates and improved terrestrial locomotion."
        ));
        listModel.addElement(new EvolutionaryStage(
                "4. Cynodonts (e.g., Procynosuchus)",
                "Late Permian - Triassic (~260 Mya)",
                "Secondary bony palate, multi-cusped cheek teeth, specialized jaw articulation.",
                "Allowed simultaneous breathing and chewing; early stages of endothermy and whiskers."
        ));
        listModel.addElement(new EvolutionaryStage(
                "5. Early Mammaliaforms (e.g., Morganucodon)",
                "Late Triassic - Early Jurassic (~205 Mya)",
                "3 middle ear ossicles (malleus, incus, stapes), diphyodont teeth, mammary glands.",
                "Transition to full endothermy, enlarged brain relative to body size, nocturnal adaptations."
        ));
        listModel.addElement(new EvolutionaryStage(
                "6. Early Primates (e.g., Plesiadapiforms / Archicebus)",
                "Early Paleocene - Eocene (~66-55 Mya)",
                "Forward-facing eyes (stereoscopic vision), grasping hands/feet with nails, enlarged visual cortex.",
                "Adaptation to arboreal life in canopy environments following Cretaceous extinction."
        ));
        listModel.addElement(new EvolutionaryStage(
                "7. Hominids (e.g., Australopithecus afarensis)",
                "Pliocene (~4-3 Mya)",
                "Habitual bipedal locomotion, angled femur, remodeled pelvis, reduced canine size.",
                "Transition from canopy to open savanna; freed hands for tool use and transport."
        ));
        listModel.addElement(new EvolutionaryStage(
                "8. Homo Sapiens",
                "Late Pleistocene - Present (~300,000 ya)",
                "High globular cranium, prominent chin, light skeletal build, average brain volume ~1350 cc.",
                "Complex symbolic language, advanced culture, abstract reasoning, and global dominance."
        ));

        stageList = new JList<>(listModel);
        stageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        stageList.setFont(new Font("SansSerif", Font.BOLD, 13));

        // Display components
        titleLabel = new JLabel("Select a stage on the left", SwingConstants.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 60, 90));

        periodLabel = new JLabel("Geological Period: -");
        periodLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));

        featuresArea = new JTextArea(4, 30);
        featuresArea.setEditable(false);
        featuresArea.setLineWrap(true);
        featuresArea.setWrapStyleWord(true);
        featuresArea.setFont(new Font("SansSerif", Font.PLAIN, 13));

        significanceArea = new JTextArea(4, 30);
        significanceArea.setEditable(false);
        significanceArea.setLineWrap(true);
        significanceArea.setWrapStyleWord(true);
        significanceArea.setFont(new Font("SansSerif", Font.PLAIN, 13));

        // Layout arrangement
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        detailPanel.add(titleLabel);
        detailPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailPanel.add(periodLabel);
        detailPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        detailPanel.add(new JLabel("Key Anatomical Features:"));
        detailPanel.add(new JScrollPane(featuresArea));
        detailPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        detailPanel.add(new JLabel("Evolutionary Significance:"));
        detailPanel.add(new JScrollPane(significanceArea));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(stageList), detailPanel);
        splitPane.setDividerLocation(260);

        add(splitPane);

        // Selection listener to update details
        stageList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    EvolutionaryStage selected = stageList.getSelectedValue();
                    if (selected != null) {
                        titleLabel.setText(selected.stageName);
                        periodLabel.setText("Geological Timeline: " + selected.period);
                        featuresArea.setText(selected.anatomicalFeatures);
                        significanceArea.setText(selected.evolutionarySignificance);
                    }
                }
            }
        });

        stageList.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AnimalEvolutionFromReptilestoHomoSapiensApp().setVisible(true);
        });
    }
}