using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Common;
using System.Text;




namespace DBLib
{
    class DBUtils
    {
        public const int ROWS_PER_PAGE = 5;
        
        public static void afegirParametre(
            DbCommand comanda,
            String nomParametre,
            object valorParametre,
            DbType tipus)
        {
            DbParameter p = comanda.CreateParameter();
            p.ParameterName = nomParametre;
            p.Value = valorParametre;
            p.DbType = tipus;
            comanda.Parameters.Add(p);
        }



        public static T? readDB<T>(DbDataReader reader, string columna)
            where T : struct
        {
            if (!reader.IsDBNull(reader.GetOrdinal(columna)))
            {
                T valor = default(T);
                int numColumna = reader.GetOrdinal(columna);
                if (valor is decimal)
                {
                    valor = (T)(object)reader.GetDecimal(numColumna);
                }
                else if (valor is int)
                {
                    valor = (T)(object)reader.GetInt32(numColumna);
                }
                else if (valor is DateTime)
                {
                    valor = (T)(object)reader.GetDateTime(numColumna);
                }
                else
                {
                    throw new Exception("Tipus no suportat");
                }
                return valor;
            }
            else return null;
        }


        public static T readDBC<T>(DbDataReader reader, string columna)
            where T : class
        {
            if (!reader.IsDBNull(reader.GetOrdinal(columna)))
            {
                T valor = default(T);
                int numColumna = reader.GetOrdinal(columna);

                if (typeof(T) == typeof(String))
                {
                    valor = (T)(object)reader.GetString(numColumna);
                }
                else
                {
                    throw new Exception("Tipus no suportat");
                }
                return valor;
            }
            else return null;
        }

        public static long count(String nomTaula)
        {
            try
            {
                using (MySQLDbContext context = new MySQLDbContext())
                {
                    using (var connection = context.Database.GetDbConnection())
                    {
                        connection.Open();
                        using (var comanda = connection.CreateCommand())
                        {
                            comanda.CommandText = "select count(1) from " + nomTaula;
                            return (long)comanda.ExecuteScalar();
                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }
            return 0;
        }
    } // end class


}// end namespace
