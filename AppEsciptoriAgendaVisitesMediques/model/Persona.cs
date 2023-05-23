using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AppEsciptoriAgendaVisitesMediques.model
{
    class Persona
    {

        private int codi;
        private String nif;
        private String nom;
        private String cognom1;
        private String cognom2;
        private String adreca;
        private String poblacio;
        private int sexe;
        private String login;
        private String password;
        private bool esMetge;

        public Persona(int codi, String nif, String nom, String cognom1, String cognom2, String adreca, String poblacio, int sexe, String login, String password, bool esMetge)
        {
            this.codi = codi;
            this.nif = nif;
            this.nom = nom;
            this.cognom1 = cognom1;
            this.cognom2 = cognom2;
            this.adreca = adreca;
            this.poblacio = poblacio;
            this.sexe = sexe;
            this.login = login;
            this.password = password;
            this.esMetge = esMetge;
        }

        public int Codi
        {
            get { return codi; }
            set { codi = value; }
        }

        public String Nif
        {
            get { return nif; }
            set { nif = value; }
        }

        public String Nom
        {
            get { return nom; }
            set { nom = value; }
        }

        public String Cognom1
        {
            get { return cognom1; }
            set { cognom1 = value; }
        }

        public String Cognom2
        {
            get { return cognom2; }
            set { cognom2 = value; }
        }

        public String Adreca
        {
            get { return adreca; }
            set { adreca = value; }
        }

        public String Poblacio
        {
            get { return poblacio; }
            set { poblacio = value; }
        }

        public int Sexe
        {
            get { return sexe; }
            set { sexe = value; }
        }

        public String Login
        {
            get { return login; }
            set { login = value; }
        }

        public String Password
        {
            get { return password; }
            set { password = value; }
        }

        public bool EsMetge
        {
            get { return esMetge; }
            set { esMetge = value; }
        }

        public override bool Equals(Object obj)
        {
            if (obj == null || GetType() != obj.GetType())
            {
                return false;
            }

            Persona p = (Persona)obj;

            return Codi == p.Codi
                && Nif.Equals(p.Nif)
                && Nom.Equals(p.Nom)
                && Cognom1.Equals(p.Cognom1)
                && Cognom2.Equals(p.Cognom2)
                && Adreca.Equals(p.Adreca)
                && Poblacio.Equals(p.Poblacio)
                && Sexe == p.Sexe
                && Login.Equals(p.Login)
                && Password.Equals(p.Password)
                && EsMetge == p.EsMetge;
        }

        public override int GetHashCode()
        {
            return Codi.GetHashCode();
        }
    }
}
