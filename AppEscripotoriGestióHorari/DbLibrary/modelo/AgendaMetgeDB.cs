using Microsoft.EntityFrameworkCore;
using MySql.Data.MySqlClient;
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
    public class AgendaMetgeDB
    {

        private int codiMetge;
        private int codiEspecialitat;
        private TimeSpan hora;
        private String diaSetmana;

        public int CodiMetge { get => codiMetge; set => codiMetge = value; }
        public int CodiEspecialitat { get => codiEspecialitat; set => codiEspecialitat = value; }
        public TimeSpan Hora { get => hora; set => hora = value; }
        public string DiaSetmana { get => diaSetmana; set => diaSetmana = value; }


        public AgendaMetgeDB(int codiMetge, TimeSpan hora, string diaSetmana, int codiEspecialitat)
        {
            this.codiMetge = codiMetge;
            CodiEspecialitat = codiEspecialitat;
            Hora = hora;
            DiaSetmana = diaSetmana;
        }
        public AgendaMetgeDB(int codiMetge, TimeSpan hora, string diaSetmana)
        {
            CodiMetge = codiMetge;
            Hora = hora;
            DiaSetmana = diaSetmana;
        }

        public override bool Equals(object obj)
        {
            return obj is AgendaMetgeDB dB &&
                   codiMetge == dB.codiMetge &&
                   codiEspecialitat == dB.codiEspecialitat &&
                   hora.Equals(dB.hora) &&
                   diaSetmana == dB.diaSetmana &&
                   CodiMetge == dB.CodiMetge &&
                   CodiEspecialitat == dB.CodiEspecialitat &&
                   Hora.Equals(dB.Hora) &&
                   DiaSetmana == dB.DiaSetmana;
        }

        public override int GetHashCode()
        {
            return HashCode.Combine(codiMetge, codiEspecialitat, hora, diaSetmana, CodiMetge, CodiEspecialitat, Hora, DiaSetmana);
        }

        public override string ToString()
        {
            return codiMetge.ToString() + " " + hora.ToString() + " " + diaSetmana + " " + codiEspecialitat.ToString();
        }

        public static ObservableCollection<AgendaMetgeDB> GetHorari(int codiMetge)
        {
            AgendaMetgeDB agndMetge = null;
            ObservableCollection<AgendaMetgeDB> agendasMetge = new ObservableCollection<AgendaMetgeDB>();
            try
            {
                using (MySQLDbContext context = new MySQLDbContext())
                {
                    using (var connection = context.Database.GetDbConnection())
                    {
                        connection.Open();
                        using (var comanda = connection.CreateCommand())
                        {
                            comanda.CommandText = $@"select * from agenda_metges am where am.am_me_codi = @metge_codi";

                            DBUtils.afegirParametre(comanda, "metge_codi", codiMetge, DbType.Int32);

                            DbDataReader reader = comanda.ExecuteReader();
                            while (reader.Read())
                            {
                                int id = reader.GetInt32(reader.GetOrdinal("am_me_codi"));
                                String dia = DBUtils.readDBC<String>(reader, "am_dia_setmana");
                                
                                String horaDateTime = DBUtils.readDBC<String>(reader, "am_hora");
                                TimeSpan hora;
                                hora = TimeSpan.Parse(horaDateTime);

                                // Opción 2: Utilizando TimeSpan.TryParse
                               /* if (TimeSpan.TryParse(horaDateTime, out hora))
                                {
                                    // Conversión exitosa
                                }
                                else
                                {
                                    // Manejo de error si no se puede convertir
                                }*/
                                int cosiEsp = reader.GetInt32(reader.GetOrdinal("am_esp_codi"));

                                agndMetge = new AgendaMetgeDB(id, hora, dia, cosiEsp);
                                agendasMetge.Add(agndMetge);
                            }

                            reader.Close();



                            //TipusCasellaDB[,] comprovacio = l.Caselles;
                            reader.Close();



                        }
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
            return agendasMetge;
        }

        public static Boolean insertarAgendaMetges(AgendaMetgeDB agenda)
        {
            using (MySQLDbContext context = new MySQLDbContext())
            {
                try
                {
                    using (var connection = context.Database.GetDbConnection())
                    {
                        connection.Open();
                        DbTransaction transaccio = connection.BeginTransaction();
                        using (var comanda = connection.CreateCommand())
                        {
                            DBUtils.afegirParametre(comanda, "metge", agenda.codiMetge, DbType.Int32);
                            DBUtils.afegirParametre(comanda, "dia", agenda.diaSetmana, DbType.String);
                            DBUtils.afegirParametre(comanda, "hora", agenda.hora.ToString(), DbType.String);
                            DBUtils.afegirParametre(comanda, "esp", agenda.CodiEspecialitat, DbType.Int32);

                            comanda.CommandText = $@"INSERT INTO agenda_metges (am_me_codi, am_dia_setmana, am_hora, am_esp_codi) VALUES (@metge, @dia, @hora, @esp);";

                            
                            Int32 filesActualitzades = comanda.ExecuteNonQuery();
                            if (filesActualitzades != 1)
                            {
                                transaccio.Rollback();
                                return false;
                            } else
                            {
                                transaccio.Commit();
                                return true;
                            }


                        }

                    }

                }
                catch (MySqlException ex)
                {
                    Debug.WriteLine("Fallo al insertar agenda el dia:" + agenda.diaSetmana +" hora"+ agenda.hora+ ex.ToString());
                }

            }
            return false;
        }

        public static Boolean updateAgendaMetges(AgendaMetgeDB agendaVella, AgendaMetgeDB agendaNova)
        {
            using (MySQLDbContext context = new MySQLDbContext())
            {
                try
                {
                    using (var connection = context.Database.GetDbConnection())
                    {
                        connection.Open();
                        DbTransaction transaccio = connection.BeginTransaction();
                        using (var comanda = connection.CreateCommand())
                        {
                            DBUtils.afegirParametre(comanda, "espCodi", agendaNova.codiEspecialitat, DbType.Int32);
                            DBUtils.afegirParametre(comanda, "metCodi", agendaVella.codiMetge, DbType.Int32);
                            DBUtils.afegirParametre(comanda, "hora", agendaVella.hora.ToString(), DbType.String);
                            DBUtils.afegirParametre(comanda, "dia", agendaVella.diaSetmana, DbType.String);

                            comanda.CommandText = $@"UPDATE agenda_metges am SET am.am_esp_codi  = @espCodi WHERE am.am_me_codi = @metCodi and am.am_hora  = @hora and am.am_dia_setmana = @dia";


                            Int32 filesActualitzades = comanda.ExecuteNonQuery();
                            if (filesActualitzades != 1)
                            {
                                transaccio.Rollback();
                                return false;
                            }
                            else
                            {
                                transaccio.Commit();
                                return true;
                            }


                        }

                    }

                }
                catch (MySqlException ex)
                {
                    Debug.WriteLine("Fallo al actualizar agenda con la especialidad:" + agendaNova.codiEspecialitat + ex.ToString());
                }

            }
            return false;
        }

        public static Boolean deleteAgendaMetges(AgendaMetgeDB agenda)
        {
            using (MySQLDbContext context = new MySQLDbContext())
            {
                try
                {
                    using (var connection = context.Database.GetDbConnection())
                    {
                        connection.Open();
                        DbTransaction transaccio = connection.BeginTransaction();
                        using (var comanda = connection.CreateCommand())
                        {
                            DBUtils.afegirParametre(comanda, "metCodi", agenda.codiMetge, DbType.Int32);
                            DBUtils.afegirParametre(comanda, "espCodi", agenda.CodiEspecialitat, DbType.Int32);
                            DBUtils.afegirParametre(comanda, "hora", agenda.hora.ToString(), DbType.String);
                            DBUtils.afegirParametre(comanda, "dia", agenda.diaSetmana, DbType.String);

                            comanda.CommandText = $@"delete from agenda_metges where am_me_codi = @metCodi and am_esp_codi = @espCodi and am_hora = @hora and am_dia_setmana = @dia";


                            Int32 filesActualitzades = comanda.ExecuteNonQuery();
                            if (filesActualitzades != 1)
                            {
                                transaccio.Rollback();
                                return false;
                            }
                            else
                            {
                                transaccio.Commit();
                                return true;
                            }


                        }

                    }

                }
                catch (MySqlException ex)
                {
                    Debug.WriteLine("Fallo al eliminar agenda con la especialidad:" + agenda.codiEspecialitat + ex.ToString());
                }

            }
            return false;
        }

    }

}
