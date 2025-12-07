
package analyselexprojet;
import java.util.List;
public class Analysesyntaxique {
    static List<Token> tokens;
    static int pos = 0;
    static boolean erreur = false;

    // retourne le token courant
    static Token courant() {
        if (pos < tokens.size()) return tokens.get(pos);
        return null;
    }

    static void avancer() {
        pos++;
    }

    // point d'entrée du parser
    static void Z() {
        PROGRAM();
        if (pos == tokens.size() && !erreur) {
            System.out.println("Code accepté !");
        } else {
            System.out.println("Code refusé !");
        }
    }

    // PROGRAM -> suite d'instructions
    static void PROGRAM() {
        INSTRS();
    }

    // INSTRS -> INSTR INSTRS | ε
    static void INSTRS() {
        while (pos < tokens.size() && !erreur && 
               (courant().type.equals("IDENTIFIANT") || 
                courant().valeur.equals("for") ||
                courant().valeur.equals("print"))) {
            INSTR();
        }
    }

    // INSTR -> AFFECT | FOREACH | PRINT
    static void INSTR() {
        if (courant().type.equals("IDENTIFIANT")) {
            AFFECT();
        } else if (courant().valeur.equals("for")) {
            FOREACH();
        } else if (courant().valeur.equals("print")) {
            PRINT();
        } else {
            System.out.println("Instruction ignorée : " + courant().valeur);
            
            avancer();
        }
    }

    // AFFECT -> IDENT '=' EXPR | IDENT '+=' EXPR | IDENT '-=' EXPR
    static void AFFECT() {
        if (courant().type.equals("IDENTIFIANT")) {
            avancer();
            if (courant() != null && (courant().valeur.equals("=") || 
                                      courant().valeur.equals("+=") || 
                                      courant().valeur.equals("-="))) {
                avancer();
                EXPR();
            } else {
                System.out.println("Erreur: '=' ou '+=' ou '-=' attendu");
                erreur = true;
            }
        }
    }

    // FOREACH -> 'for' IDENT 'in' IDENT ':'
    static void FOREACH() {
        if (courant().valeur.equals("for")) {
            avancer();
            if (courant().type.equals("IDENTIFIANT")) {
                avancer();
                if (courant() != null && courant().valeur.equals("in")) {
                    avancer();
                    if (courant().type.equals("IDENTIFIANT")) {
                        avancer();
                        if (courant() != null && courant().valeur.equals(":")) {
                            avancer();
                            INSTRS(); // instructions dans la boucle
                        } else {
                            System.out.println("Erreur: ':' attendu après for");
                            erreur = true;
                        }
                    } else {
                        System.out.println("Erreur: identifiant attendu après 'in'");
                        erreur = true;
                    }
                } else {
                    System.out.println("Erreur: 'in' attendu après ident");
                    erreur = true;
                }
            } else {
                System.out.println("Erreur: identifiant attendu après 'for'");
                erreur = true;
            }
        }
    }

    // PRINT -> 'print' '(' EXPR (',' EXPR)* ')'
    static void PRINT() {
        if (courant().valeur.equals("print")) {
            avancer();
            if (courant() != null && courant().valeur.equals("(")) {
                avancer();
                EXPR();
                while (courant() != null && courant().valeur.equals(",")) {
                    avancer();
                    EXPR();
                }
                if (courant() != null && courant().valeur.equals(")")) {
                    avancer();
                } else {
                    System.out.println("Erreur: ')' attendu dans print");
                    erreur = true;
                }
            } else {
                System.out.println("Erreur: '(' attendu après print");
                erreur = true;
            }
        }
    }

    // EXPR -> TERME RESTEXPR
    static void EXPR() {
        TERME();
        RESTEXPR();
    }

    // RESTEXPR -> ('+'|'-') TERME RESTEXPR | ε
    static void RESTEXPR() {
        while (courant() != null && (courant().valeur.equals("+") || courant().valeur.equals("-"))) {
            avancer();
            TERME();
        }
    }

    // TERME -> FACT RESTTERME
    static void TERME() {
        FACT();
        RESTTERME();
    }

    // RESTTERME -> ('*'|'/') FACT RESTTERME | ε
    static void RESTTERME() {
        while (courant() != null && (courant().valeur.equals("*") || courant().valeur.equals("/"))) {
            avancer();
            FACT();
        }
    }

    // FACT -> IDENT | NOMBRE | '(' EXPR ')'
  static void FACT() {
    if (courant() == null) {
        erreur = true;
        return;
    }
    if (courant().type.equals("IDENTIFIANT") || courant().type.equals("NOMBRE")) {
        avancer();
    } else if (courant().valeur.equals("(")) {
        avancer();
        EXPR();
        if (courant() != null && courant().valeur.equals(")")) {
            avancer();
        } else {
            System.out.println("Erreur: ')' attendu");
            erreur = true;
        }
    } else if (courant().valeur.equals("\"")) { // gérer les chaînes
        avancer(); // début de la chaîne
        while (courant() != null && !courant().valeur.equals("\"")) {
            avancer(); // consommer tout le contenu
        }
        if (courant() != null && courant().valeur.equals("\"")) {
            avancer(); // fin de la chaîne
        } else {
            System.out.println("Erreur: '\"' attendu pour fermer la chaîne");
            erreur = true;
        }
    } else if (courant().valeur.equals("[")) { // gérer les listes
        avancer();
        if (!courant().valeur.equals("]")) {
            EXPR();
            while (courant() != null && courant().valeur.equals(",")) {
                avancer();
                EXPR();
            }
        }
        if (courant() != null && courant().valeur.equals("]")) {
            avancer();
        } else {
            System.out.println("Erreur: ']' attendu");
            erreur = true;
        }
    } else {
        System.out.println("Erreur syntaxique sur: " + courant().valeur);
        erreur = true;
        avancer();
    }
}

}

