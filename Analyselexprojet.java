package analyselexprojet;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Analyselexprojet {
    static int [][] matrice={
        // L  C =  + - </> ! (|)|:|,;.{[]} espace|tab \n autre
        {1,2,3,4,5,6,7,8,0,-1}, //s0
        {1,1,-1,-1,-1,-1,-1,-1,-1,-1},//s1
        {-1,2,-1,-1,-1,-1,-1,-1,-1,-1},//s2
        {-1,-1,9,-1,-1,-1,-1,-1,-1,-1},//s3
        {-1,-1,11,-1,-1,-1,-1,-1,-1,-1},//s4
        {-1,-1,12,-1,-1,-1,-1,-1,-1,-1},//s5
        {-1,-1,13,-1,-1,-1,-1,-1,-1,-1},//s6
        {-1,-1,10,-1,-1,-1,-1,-1,-1,-1},//s7
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},//s8
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},//s9
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},//s10
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},//s11
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},//s12
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},//s13
    };
    static boolean etatsfinaux []={false,true,true,true,true,true,true,false,true,true,true,true,true,true};

    static int getCol(char c){
        if(Character.isLetter(c)) return 0;
        if(Character.isDigit(c)) return 1;
        if(c == '=') return 2;
        if(c == '+') return 3;
        if(c == '-') return 4;
        if(c == '<' || c == '>') return 5;
        if(c == '!') return 6;
        if(c == '('|| c ==')'|| c =='{'|| c=='}'|| c=='['|| c==']'||c==';'||c==':'|| c==','||c=='"'|| c=='.') return 7;
        if(c == ' ' || c =='\t' || c =='\n') return 8;
        return 9;
    }

    static String typeLexeme(int etat) {
        switch (etat) {
            case 1: return "IDENTIFIANT";
            case 2: return "NOMBRE";
            case 3: return "ASSIGN";
            case 4: return "PLUS";
            case 5: return "MOINS";
            case 6: return "RELATIONNEL";
            case 8: return "SYMBOLE";
            case 9: return "EGAL_EGAL";
            case 10: return "NON_EGAL";
            case 11: return "PLUS_EGAL";
            case 12: return "MOINS_EGAL";
            case 13: return "SUP_INF_EGAL";
            default: return "INCONNU";
        }
    }
    static String motCle(String lexeme) {
    switch (lexeme.toLowerCase()) {
        case "for": return "FOR";
        case "in": return "IN";
        case "print": return "PRINT";
        case "foreach": return "FOREACH"; // si besoin
        case "while": return "WHILE";
        case "if": return "IF";
        case "else": return "ELSE";
        case "elif": return "ELIF";
        case "dairi": return "NOM_PERSONNALISE";   
        case "lilia": return "PRENOM_PERSONNALISE";
        default: return "IDENTIFIANT";
    }
}

    static List<Token> analyser(String ligne) {
        List<Token> Tokens = new ArrayList<>();
        int i = 0;

        while (i < ligne.length()) {
            char c = ligne.charAt(i);

            // Ignorer les espaces
            if (c == ' ' || c == '\t' || c == '\n') {
                i++;
                continue;
            }
            if (c == '#') break;

            int etat = 0;
            int debut = i;

            while (i < ligne.length()) {
                c = ligne.charAt(i);
                int col = getCol(c);
                int etatSuivant = matrice[etat][col];

                if (etatSuivant == -1) break;

                etat = etatSuivant;
                i++;
            }

            if (etatsfinaux[etat]) {
                String lex = ligne.substring(debut, i);
                String type = typeLexeme(etat);
                if(type.equals("IDENTIFIANT")){
                type = motCle(lex);
                }
                Tokens.add(new Token(type, lex));
            } else {
                System.out.println("ERREUR LEXICALE à : " + ligne.substring(debut, i + 1));
                i++;
            }
        }
        return Tokens;
    }

    public static void main(String[] args) {
        Scanner E = new Scanner(System.in);
        while (true) {
        StringBuilder codecomplet = new StringBuilder();

        System.out.println("Veuillez saisir un code (FIN pour terminer) :");
         while(true){
        
            String ligne = E.nextLine();
            if (ligne.equals("FIN")) break;
            codecomplet.append(ligne).append("\n");
        }

        List<Token> Tokens = analyser(codecomplet.toString());

        System.out.println("Lexèmes reconnus :");
        for (Token tok : Tokens) { // foreach
            System.out.println(tok);
        }
        Analysesyntaxique.tokens = Tokens;
        Analysesyntaxique.pos = 0;
        Analysesyntaxique.erreur = false;

        Analysesyntaxique.Z();
    }
}
}

