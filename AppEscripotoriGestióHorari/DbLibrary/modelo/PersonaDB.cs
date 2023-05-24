using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Data;
using System.Data.Common;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DbLibrary.modelo
{
    public class PersonaDB
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

        public PersonaDB(int codi, String nif, String nom, String cognom1, String cognom2, String adreca, String poblacio, int sexe, String login, String password, bool esMetge)
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

        public PersonaDB()
        {

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

            PersonaDB p = (PersonaDB)obj;

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

        public static PersonaDB getPersonasDB(int codi)
        {
            PersonaDB per = null;
            ObservableCollection<PersonaDB> personas = new ObservableCollection<PersonaDB>();
            using (MySQLDbContext context = new MySQLDbContext())
            {
                using (var connection = context.Database.GetDbConnection())
                {
                    connection.Open();
                    using (var comanda = connection.CreateCommand())
                    {
                        comanda.CommandText = $@"select * from persona where persona.per_codi = @p_codi ";

                        DBUtils.afegirParametre(comanda, "p_codi", codi, DbType.Int32);

                        DbDataReader reader = comanda.ExecuteReader();
                        while (reader.Read())
                        {
                            int id = reader.GetInt32(reader.GetOrdinal("per_codi"));
                            String nif = DBUtils.readDBC<String>(reader, "per_nif");
                            String nom = DBUtils.readDBC<String>(reader, "per_nom");
                            String cognom1 = DBUtils.readDBC<String>(reader, "per_cognom1");
                            String cognom2 = DBUtils.readDBC<String>(reader, "per_cognom2");
                            String adreca = DBUtils.readDBC<String>(reader, "per_adreca");
                            String poblacio = DBUtils.readDBC<String>(reader, "per_poblacio");
                            int sexe = reader.GetInt32(reader.GetOrdinal("per_sexe"));
                            String login = DBUtils.readDBC<String>(reader, "per_login");
                            String passw = DBUtils.readDBC<String>(reader, "per_passw");
                            int esMetge = reader.GetInt32(reader.GetOrdinal("per_es_metge"));
                            bool isMetge;
                            if (esMetge == 0)
                            {
                                isMetge = false;
                            }
                            else
                            {
                                isMetge = true;
                            }
                            per = new PersonaDB(id, nif, nom, cognom1, cognom2, adreca, poblacio, sexe, login, passw, isMetge);




                            //personas.Add(per);
                        }

                        reader.Close();



                        //TipusCasellaDB[,] comprovacio = l.Caselles;
                        reader.Close();



                    }
                }
            }
            return per;
        }
    }
}
