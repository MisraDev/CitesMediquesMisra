using DBLib;
using Microsoft.EntityFrameworkCore;
using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Data;
using System.Data.Common;
using System.Diagnostics;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DbLib.Model
{
    public class CitaDB
    {
        private int codiCita;

        private DateTime dataCita;

        private String informe;

        private PersonaDB persona;

        private PersonaDB metge;

        //private Especialitats especialitat;

        public CitaDB()
        {

        }

        public CitaDB(int codiCíta, DateTime dataCita, String infrome, PersonaDB persona, PersonaDB metge)
        {
            this.codiCita = codiCíta;
            this.dataCita = dataCita;
            this.informe = infrome;
            this.persona = persona;
            this.metge = metge;
        }

        public int CodiCita
        {
            get { return codiCita; }
            set { codiCita = value; }
        }

        public DateTime DataCita
        {
            get { return dataCita; }
            set { dataCita = value; }
        }

        public String Informe
        {
            get { return informe; }
            set { informe = value; }
        }

        public PersonaDB Persona
        {
            get { return persona; }
            set { persona = value; }
        }

        public PersonaDB Metge
        {
            get { return metge; }
            set { metge = value; }
        }

        //public Especialitats Especialitat
        //{
        //    get { return especialitat; }
        //    set { especialitat = value; }
        //}

        public override int GetHashCode()
        {
            return codiCita.GetHashCode();
        }

        public override bool Equals(object obj)
        {
            if (obj == null || GetType() != obj.GetType())
            {
                return false;
            }

            CitaDB other = (CitaDB)obj;
            return codiCita == other.codiCita;
        }

        public override string ToString()
        {
            //return this.Persona.Nom;
            return "Cita "+ this.Persona.Nom;
        }

        public static ObservableCollection<CitaDB> getCitasDB(int codiMetge)
        {
            ObservableCollection<CitaDB> citas = new ObservableCollection<CitaDB>();
            using (MySQLDbContext context = new MySQLDbContext())
            {
                try
                {
                    using (var connection = context.Database.GetDbConnection())
                    {
                        connection.Open();
                        using (var comanda = connection.CreateCommand())
                        {
                            comanda.CommandText = $@"select * from cita c where c.cit_codi_empleat = @c_codi ";

                            DBUtils.afegirParametre(comanda, "c_codi", codiMetge, DbType.Int32);

                            DbDataReader reader = comanda.ExecuteReader();
                            while (reader.Read())
                            {
                                int id = reader.GetInt32(reader.GetOrdinal("cit_codi"));
                                DateTime dataHora = reader.GetDateTime(reader.GetOrdinal("cit_data_hora"));
                                try
                                {
                                    dataHora = reader.GetDateTime(reader.GetOrdinal("cit_data_hora"));
                                    // Resto del código...
                                }
                                catch (MySqlException ex)
                                {
                                    Debug.WriteLine("Error al recuperar el campo 'cit_data_hora': " + ex.Message);
                                    // Manejar el error adecuadamente
                                    continue;
                                }


                       
                                //DateTime? dataHORA = DBUtils.readDB<DateTime>(reader, "cit_data_hora");
                                //String dataHORA = DBUtils.readDBC<String>(reader, "cit_data_hora");
                              
                                String informe = DBUtils.readDBC<String>(reader, "cit_informe");
                                int? persona = DBUtils.readDB<Int32>(reader, "cit_codi_persona");
                                int? metge = DBUtils.readDB<Int32>(reader, "cit_codi_empleat");


                                CitaDB cita = new CitaDB(id, dataHora, informe, PersonaDB.getPersonasDB((int)persona), PersonaDB.getPersonasDB(codiMetge));



                                citas.Add(cita);
                            }

                            reader.Close();







                        }
                    }
                }
                catch (Exception ex)
                {
                    Debug.WriteLine("Fallo al recuperar citas del metge" + codiMetge + ex.ToString());
                }
                
            }
            return citas;
        }

        public static ObservableCollection<CitaDB> getCitasDBSemanaActual(int codiMetge)
        {
            ObservableCollection<CitaDB> citas = new ObservableCollection<CitaDB>();
            using (MySQLDbContext context = new MySQLDbContext())
            {
                try
                {
                    using (var connection = context.Database.GetDbConnection())
                    {
                        connection.Open();
                        using (var comanda = connection.CreateCommand())
                        {
                            comanda.CommandText = $@"select * from cita c where c.cit_codi_empleat = @c_codi and YEARWEEK(c.cit_data_hora) = YEARWEEK(curdate()) ";

                            DBUtils.afegirParametre(comanda, "c_codi", codiMetge, DbType.Int32);

                            DbDataReader reader = comanda.ExecuteReader();
                            while (reader.Read())
                            {
                                int id = reader.GetInt32(reader.GetOrdinal("cit_codi"));
                                DateTime dataHora = reader.GetDateTime(reader.GetOrdinal("cit_data_hora"));
                                try
                                {
                                    dataHora = reader.GetDateTime(reader.GetOrdinal("cit_data_hora"));
                                    // Resto del código...
                                }
                                catch (MySqlException ex)
                                {
                                    Debug.WriteLine("Error al recuperar el campo 'cit_data_hora': " + ex.Message);
                                    // Manejar el error adecuadamente
                                    continue;
                                }



                                //DateTime? dataHORA = DBUtils.readDB<DateTime>(reader, "cit_data_hora");
                                //String dataHORA = DBUtils.readDBC<String>(reader, "cit_data_hora");

                                String informe = DBUtils.readDBC<String>(reader, "cit_informe");
                                int? persona = DBUtils.readDB<Int32>(reader, "cit_codi_persona");
                                int? metge = DBUtils.readDB<Int32>(reader, "cit_codi_empleat");


                                CitaDB cita = new CitaDB(id, dataHora, informe, PersonaDB.getPersonasDB((int)persona), PersonaDB.getPersonasDB(codiMetge));



                                citas.Add(cita);
                            }

                            reader.Close();







                        }
                    }
                }
                catch (Exception ex)
                {
                    Debug.WriteLine("Fallo al recuperar citas del metge" + codiMetge + ex.ToString());
                }

            }
            return citas;
        }

        public static ObservableCollection<CitaDB> getCitasDBSemanaSiguiente(int codiMetge)
        {
            ObservableCollection<CitaDB> citas = new ObservableCollection<CitaDB>();
            using (MySQLDbContext context = new MySQLDbContext())
            {
                try
                {
                    using (var connection = context.Database.GetDbConnection())
                    {
                        connection.Open();
                        using (var comanda = connection.CreateCommand())
                        {
                            comanda.CommandText = $@"select * from cita c where c.cit_codi_empleat = @c_codi and YEARWEEK(c.cit_data_hora) = YEARWEEK(curdate() + INTERVAL 1 WEEK) ";

                            DBUtils.afegirParametre(comanda, "c_codi", codiMetge, DbType.Int32);

                            DbDataReader reader = comanda.ExecuteReader();
                            while (reader.Read())
                            {
                                int id = reader.GetInt32(reader.GetOrdinal("cit_codi"));
                                DateTime dataHora = reader.GetDateTime(reader.GetOrdinal("cit_data_hora"));
                                try
                                {
                                    dataHora = reader.GetDateTime(reader.GetOrdinal("cit_data_hora"));
                                    // Resto del código...
                                }
                                catch (MySqlException ex)
                                {
                                    Debug.WriteLine("Error al recuperar el campo 'cit_data_hora': " + ex.Message);
                                    // Manejar el error adecuadamente
                                    continue;
                                }



                                //DateTime? dataHORA = DBUtils.readDB<DateTime>(reader, "cit_data_hora");
                                //String dataHORA = DBUtils.readDBC<String>(reader, "cit_data_hora");

                                String informe = DBUtils.readDBC<String>(reader, "cit_informe");
                                int? persona = DBUtils.readDB<Int32>(reader, "cit_codi_persona");
                                int? metge = DBUtils.readDB<Int32>(reader, "cit_codi_empleat");


                                CitaDB cita = new CitaDB(id, dataHora, informe, PersonaDB.getPersonasDB((int)persona), PersonaDB.getPersonasDB(codiMetge));



                                citas.Add(cita);
                            }

                            reader.Close();







                        }
                    }
                }
                catch (Exception ex)
                {
                    Debug.WriteLine("Fallo al recuperar citas del metge" + codiMetge + ex.ToString());
                }

            }
            return citas;
        }

        public static ObservableCollection<CitaDB> getCitasDBSemanaAnterior(int codiMetge)
        {
            ObservableCollection<CitaDB> citas = new ObservableCollection<CitaDB>();
            using (MySQLDbContext context = new MySQLDbContext())
            {
                try
                {
                    using (var connection = context.Database.GetDbConnection())
                    {
                        connection.Open();
                        using (var comanda = connection.CreateCommand())
                        {
                            comanda.CommandText = $@"select * from cita c where c.cit_codi_empleat = @c_codi and YEARWEEK(c.cit_data_hora) = YEARWEEK(curdate() - INTERVAL 1 WEEK) ";

                            DBUtils.afegirParametre(comanda, "c_codi", codiMetge, DbType.Int32);

                            DbDataReader reader = comanda.ExecuteReader();
                            while (reader.Read())
                            {
                                int id = reader.GetInt32(reader.GetOrdinal("cit_codi"));
                                DateTime dataHora = reader.GetDateTime(reader.GetOrdinal("cit_data_hora"));
                                try
                                {
                                    dataHora = reader.GetDateTime(reader.GetOrdinal("cit_data_hora"));
                                    // Resto del código...
                                }
                                catch (MySqlException ex)
                                {
                                    Debug.WriteLine("Error al recuperar el campo 'cit_data_hora': " + ex.Message);
                                    // Manejar el error adecuadamente
                                    continue;
                                }



                                //DateTime? dataHORA = DBUtils.readDB<DateTime>(reader, "cit_data_hora");
                                //String dataHORA = DBUtils.readDBC<String>(reader, "cit_data_hora");

                                String informe = DBUtils.readDBC<String>(reader, "cit_informe");
                                int? persona = DBUtils.readDB<Int32>(reader, "cit_codi_persona");
                                int? metge = DBUtils.readDB<Int32>(reader, "cit_codi_empleat");


                                CitaDB cita = new CitaDB(id, dataHora, informe, PersonaDB.getPersonasDB((int)persona), PersonaDB.getPersonasDB(codiMetge));



                                citas.Add(cita);
                            }

                            reader.Close();







                        }
                    }
                }
                catch (Exception ex)
                {
                    Debug.WriteLine("Fallo al recuperar citas del metge" + codiMetge + ex.ToString());
                }

            }
            return citas;
        }

        public static Boolean updateInformeCita(CitaDB cita)
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
                            comanda.CommandText = $@"update cita c set c.cit_informe = @informe where c.cit_codi_empleat = @codiEmpleat and c.cit_codi_persona = @codiPersona and c.cit_data_hora = @dataHora";

                            DBUtils.afegirParametre(comanda, "informe", cita.informe, DbType.String);
                            DBUtils.afegirParametre(comanda, "codiEmpleat", cita.metge.Codi, DbType.Int32);
                            DBUtils.afegirParametre(comanda, "codiPersona", cita.persona.Codi, DbType.Int32);
                            DBUtils.afegirParametre(comanda, "dataHora", cita.dataCita, DbType.DateTime);
                            Int32 filesActualitzades = comanda.ExecuteNonQuery();
                            if (filesActualitzades == 1)
                            {
                                transaccio.Commit();
                                return true;
                            }
                            

                        }

                    }

                }
                catch(MySqlException ex)
                {
                    Debug.WriteLine("Fallo al actualizar cita con informe:" + cita.informe + ex.ToString());
                }

            }
            return false;
        }



    }
}
