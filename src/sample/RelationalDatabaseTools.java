package sample;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Relational Database Tools user interface.
 * @author Raymond Cho
 *
 */
public class RelationalDatabaseTools {

    static String textBox_1;
    static String textBox_2;

    static List<String> ClosureSet = new ArrayList<>();
    static List<String> KeySet = new ArrayList<>();
    static List<String> SuperKeySet = new ArrayList<>();
    static List<String> MinCoverSet = new ArrayList<>();
    static List<String> TrivialFDSet = new ArrayList<>();
    static List<String> CheckNormalForms = new ArrayList<>();
    static List<String> ThreeNFRelations = new ArrayList<>();
    static List<String> BCNFRelations = new ArrayList<>();

    static String minCoverDetails;
    static String threeNFDetails;
    static String BCNFDetails;

    static Relation relation = null;


    public static void Calculate() {

        textBox_1 = Main.relation.getText();
        textBox_2 = Main.functionalDependencies.getText();

        ClosureSet.clear();
        KeySet.clear();
        SuperKeySet.clear();
        MinCoverSet.clear();
        TrivialFDSet.clear();
        CheckNormalForms.clear();
        ThreeNFRelations.clear();
        BCNFRelations.clear();

        String completeRelation = textBox_1.toUpperCase();
        textBox_1 = (completeRelation);
        String completeFDs = textBox_2.toUpperCase();
        textBox_2 = (completeFDs);

        if (Relation.isNullOrEmpty(completeRelation)) {
            System.out.println("Input relation schema is empty.");
            return;
        }
        if (!Relation.schemaContainsSafeChars(completeRelation)) {
            System.out.println("Input relation schema must only contain letters, commas, and parenthesis.");
            return;
        }
        if (!Relation.schemaContainsParenthesisPair(completeRelation)) {
            System.out.println("Input relation schema must contain only one pair of properly formatted parenthesis: R( )");
            return;
        }
        relation = new Relation(completeRelation);
        if (relation.getAttributes().isEmpty()) {
            System.out.println("Input relation schema has no attributes.");
            return;
        }
        if (!Relation.functionalContainsSafeChars(completeFDs)) {
            System.out.println("Input functional dependencies must only contain letters, commas, semi-colons, hyphens, and greater-than.");
            return;
        }

        if (!relation.hasPassedIntegrityChecks()) {
            System.out.println(relation.getIntegrityCheckErrorMsg());
            return;
        }
        boolean functionalCheck = Relation.functionalContainsAtLeastOneDependency(completeFDs);

        String outputErrorCheck = "";
        if (!functionalCheck) {
            outputErrorCheck += "Warning: encountered a functional dependency that is "
                    + "incomplete or improperly formatted or input functional dependencies is empty. ";
        }

        if (!outputErrorCheck.isEmpty()) {
            System.out.println(outputErrorCheck);
        }
        relation.addFunctionalDependencies(completeFDs);

        if (!relation.hasPassedIntegrityChecks()) {
            System.out.println(relation.getIntegrityCheckErrorMsg());
            return;
        }
        Normalize();
    }


    public static void Normalize() {

        CalculateClosure.improvedCalculateClosures(relation);

        List<Closure> calculatedClosures = relation.getClosures();
        List<Closure> minimumKeys = relation.getMinimumKeyClosures();

        List<Closure> superKeys = relation.getSuperKeyClosures();
        for (Closure closure : calculatedClosures) {
            List<Attribute> left = closure.getClosureOf();
            List<Attribute> right = closure.getClosure();
            String fd = ("{");
            for (int i = 0; i < left.size(); i++) {
                fd += (left.get(i).getName());
                if (i < left.size() - 1) {
                    fd += (", ");
                }
            }
            fd += ("}+ = {");
            for (int i = 0; i < right.size(); i++) {
                fd += (right.get(i).getName());
                if (i < right.size() - 1) {
                    fd += (", ");
                }
            }
            fd += ("}");

            ClosureSet.add(fd);

            if (minimumKeys.contains(closure)) {
                KeySet.add(fd.substring(0,fd.indexOf("+")));
            }
            if (superKeys.contains(closure)) {
                SuperKeySet.add(fd.substring(0,fd.indexOf("+")));
            }
        }

        // Print out minimal cover of functional dependencies

        MinimalFDCover.determineMinimalCover(relation);

        minCoverDetails = "Calculating a minimal cover set (F_min) of functional dependencies from given input:\n" +
                "(note that functional dependencies with common left-hand sides have their right-hand sides combined)\n";
        List<FunctionalDependency> minimalCover = relation.getMinimalCover();
        List<String> minimalCoverOutput = relation.getMinimalCoverOutput();

        for (String output : minimalCoverOutput) {
            minCoverDetails += (output) + "\n";
        }

        minCoverDetails += "\n";
        if (minimalCover.isEmpty()) {

        } else {
            for (int i = 0; i < minimalCover.size(); i++) {
                MinCoverSet.add(minimalCover.get(i).getFDName());
            }

            // Print out derived functional dependencies
            CalculateFDs.calculateDerivedFDs(relation);
            List<FunctionalDependency> derivedFDs = relation.getDerivedFDs();
            if (derivedFDs.isEmpty()) {
            } else {

                for (int i = 0; i < derivedFDs.size(); i++) {
                    TrivialFDSet.add(derivedFDs.get(i).getFDName());
                }
            }


            // Display normal forms
            relation.determineNormalForms();
            DetermineNormalForms normalForms = relation.getNormalFormsResults();

            CheckNormalForms.add(normalForms.getFirstNormalFormMsg());
            CheckNormalForms.add(normalForms.getSecondNormalFormMsg());
            CheckNormalForms.add(normalForms.getThirdNormalFormMsg());
            CheckNormalForms.add(normalForms.getBCNFMsg());


            // Output 3NF decomposition

            threeNFDetails = ("Decomposing input relation into 3NF using canonical functional dependency cover (lossless and preserving all minimal cover set functional dependencies):\n");
            Calculate3NFDecomposition threeNF = new Calculate3NFDecomposition(relation);
            if (normalForms.isIn3NF()) {
                threeNFDetails += ("Input relation is already in 3NF. No decomposition necessary.\n");
            } else {
                threeNF.decompose();
                threeNFDetails += (threeNF.getOutputMsg())+"\n";
                List<Relation> output3NFRelations = threeNF.getOutputRelations();
                for (Relation r : output3NFRelations) {
                    threeNFDetails += (r.printRelation()) + "\n";
                    ThreeNFRelations.add(r.printRelation());
                }
            }


            // Output BCNF decomposition
            BCNFDetails = ("Decomposing input relation into BCNF relations (lossless but not necessarily functional dependency preserving)\n");
            if (normalForms.isInBCNF()) {
                BCNFDetails += ("Input relation is already in BCNF. No decomposition necessary. ");
            } else {
                if (normalForms.isIn3NF()) {
                    threeNF.decompose();
                }
                CalculateBCNFDecomposition bcnf = new CalculateBCNFDecomposition(threeNF);
                bcnf.decompose();

                // Next display 3NF relation source BCNF decomposition
                BCNFDetails += ("Decomposing input relation into BCNF relations using decomposed 3NF relations and the minimal cover set of functional dependencies as sources.\n");
                if (bcnf.getThreeNFDecomposedWithDuplicates().size() == bcnf.getThreeNFDecomposedRs().size()) {
                    BCNFDetails += ("Final set of decomposed BCNF relations:\n");
                    for (Relation r : bcnf.getThreeNFDecomposedRs()) {
                        BCNFDetails += (r.printRelation()) + "\n";
                        BCNFRelations.add(r.printRelation());
                    }
                } else {
                    BCNFDetails += ("Initial set of decomposed BCNF relations:\n");
                    for (Relation r : bcnf.getThreeNFDecomposedWithDuplicates()) {
                        BCNFDetails += (r.printRelation()) + "\n";
                        BCNFRelations.add(r.printRelation());
                    }
                    BCNFDetails += ("Final set of decomposed BCNF relations (removing duplicate and subset relations):\n");
                    for (Relation r : bcnf.getThreeNFDecomposedRs()) {
                        BCNFDetails += (r.printRelation()) + "\n";
                        BCNFRelations.add(r.printRelation());
                    }
                }
                List<FunctionalDependency> threeNFLostFDs = bcnf.getThreeNFLostFDs();
                if (threeNFLostFDs.isEmpty()) {
                    BCNFDetails += ("No functional dependencies from the minimal cover set were lost.\n");
                } else {
                    if (threeNFLostFDs.size() == 1) {
                        BCNFDetails += ("The following minimal cover set functional dependency was lost:\n");
                    } else {
                        BCNFDetails += ("The following minimal cover set functional dependencies were lost:\n");
                    }
                    for (int i = 0; i < threeNFLostFDs.size(); i++) {
                        BCNFDetails += (threeNFLostFDs.get(i).getFDName());
                        if (i < threeNFLostFDs.size() - 1) {
                            BCNFDetails += ("; ");
                        }
                    }
                    BCNFDetails += (".");
                }

                BCNFDetails += (bcnf.getOutputMsg());
                List<Relation> resultsWithDuplicates = bcnf.getResultWithDuplicates();
                List<Relation> outputBCNFRelations = bcnf.getOutputRelations();
                if (resultsWithDuplicates.size() == outputBCNFRelations.size()) {
                    for (Relation r : outputBCNFRelations) {
                        BCNFDetails += (r.printRelation()) + "\n";
                        BCNFRelations.add(r.printRelation());
                    }
                } else {
                    BCNFDetails += ("Initial set of decomposed BCNF relations: ");
                    for (Relation r : resultsWithDuplicates) {
                        BCNFDetails += (r.printRelation()) + "\n";
                    }
                    BCNFDetails += ("Final set of decomposed BCNF relations (removing duplicate and subset relations): ");
                    for (Relation r : outputBCNFRelations) {
                        BCNFDetails += (r.printRelation()) + "\n";
                        BCNFRelations.add(r.printRelation());
                    }
                }

            }

        }
    }
}
