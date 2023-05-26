using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Data;
using System.Data.Common;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DbLibrary.modelo
{
    public class EspecialitatDB
    {

        private int codi;

        private String nomEspecialitat;

        

        public EspecialitatDB()
        {

        }

        public EspecialitatDB(int codi, String nomEspecialitat)
        {
            this.codi = codi;
            this.nomEspecialitat = nomEspecialitat;

        }

        public int Codi { get => codi; set => codi = value; }
        public string NomEspecialitat { get => nomEspecialitat; set => nomEspecialitat = value; }

        public override string ToString()
        {
            return nomEspecialitat;
        }

        public override bool Equals(object obj)
        {
            return obj is EspecialitatDB dB &&
                   codi == dB.codi &&
                   nomEspecialitat == dB.nomEspecialitat &&
                   Codi == dB.Codi &&
                   NomEspecialitat == dB.NomEspecialitat;
        }

        public override int GetHashCode()
        {
            return HashCode.Combine(codi, nomEspecialitat, Codi, NomEspecialitat);
        }

        public static ObservableCollection<EspecialitatDB> getEspecialitatsDBPerMetge(int codiMetge)
        {
            ObservableCollection<EspecialitatDB> especialitatsMetge = new ObservableCollection<EspecialitatDB>();
            using (MySQLDbContext context = new MySQLDbContext())
            {
                try
                {
                    using (var connection = context.Database.GetDbConnection())
                    {
                        connection.Open();
                        using (var comanda = connection.CreateCommand())
                        {
                            comanda.CommandText = $@"select e.esp_codi as esp_codi  , e.esp_nom as esp_nom  from metge_especialitat me join especialitat e on e.esp_codi = me.me_esp_codi where me.me_met_codi = @c_metge ";

                            DBUtils.afegirParametre(comanda, "c_metge", codiMetge, DbType.Int32);
                            especialitatsMetge.Add(new EspecialitatDB(0, "N/D"));
                            DbDataReader reader = comanda.ExecuteReader();
                            while (reader.Read())
                            {
                                int id = reader.GetInt32(reader.GetOrdinal("esp_codi"));
                                String nom = DBUtils.readDBC<String>(reader, "esp_nom");


                                EspecialitatDB esp = new EspecialitatDB(id, nom);
                                especialitatsMetge.Add(esp);
                            }

                            reader.Close();
                        }
                    }
                } catch(Exception ex)
                {
                    Debug.WriteLine("Fallo al recuperar especialitats del metge" + codiMetge + ex.ToString());
                }
            }
            return especialitatsMetge;
        }


    }
}
