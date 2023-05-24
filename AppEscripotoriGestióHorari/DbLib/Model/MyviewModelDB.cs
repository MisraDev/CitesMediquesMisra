using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DbLib.Model
{
    public class MyviewModelDB
    {

        public List<string> Horas { get; } = GetListaHoras();
        public List<string> Fechas { get; } = ObtenirDatesDeLaSetmanaActual();
        private List<CitaDB> Citas { get; } = ObtenerCitas();

        private static List<CitaDB> ObtenerCitas()
        {
            PersonaDB persona = new PersonaDB(1, "12345678A", "Juan", "Pérez", "García", "Calle Mayor 1", "Barcelona", 1, "juanperez", "password", false);
            PersonaDB metge = new PersonaDB(2, "23456789B", "Pablo", "González", "Martínez", "Calle Gran 2", "Barcelona", 1, "pablogonzalez", "password", true);
            List<CitaDB> citas = new List<CitaDB>();
            citas.Add(new CitaDB(1, DateTime.Now, "sodhifdslkj", persona, metge));
            citas.Add(new CitaDB(1, new DateTime(2023, 5, 15, 10, 0, 0), "sodhifdslkj", persona, metge));
            citas.Add(new CitaDB(2, new DateTime(2023, 5, 16, 15, 30, 0), "sodhifdslkj", persona, metge));
            citas.Add(new CitaDB(3, new DateTime(2023, 5, 18, 11, 15, 0), "sodhifdslkj", persona, metge));
            citas.Add(new CitaDB(4, new DateTime(2023, 5, 19, 16, 0, 0), "sodhifdslkj", persona, metge));
            citas.Add(new CitaDB(5, new DateTime(2023, 5, 20, 9, 0, 0), "sodhifdslkj", persona, metge));

            return citas;
        }

        public static List<string> ObtenirDatesDeLaSetmanaActual()
        {
            List<string> dates = new List<string>();

            // Obtenim la data actual y el dia de la setmana actual
            DateTime dataActual = DateTime.Today;
            DayOfWeek diaActual = dataActual.DayOfWeek;

            // Calculem el primer dia de la setmana
            DateTime primerDiaDeLaSetmana = dataActual.AddDays((-(int)diaActual) + 1);

            // Afegim les dates de cada dia de la setmana actual a la llista
            for (int i = 0; i < 7; i++)
            {
                DateTime data = primerDiaDeLaSetmana.AddDays(i);
                string dataFormatada = data.ToString("dd/MM/yyyy");
                string nomDelDia = data.ToString("dddd", new CultureInfo("ca-ES"));
                nomDelDia = char.ToUpper(nomDelDia[0]) + nomDelDia.Substring(1);
                dates.Add(nomDelDia + "\n" + dataFormatada);
            }

            return dates;
        }

        public static List<string> GetListaHoras()
        {
            List<string> hores = new List<string>();
            DateTime horaInicio = new DateTime(DateTime.Now.Year, DateTime.Now.Month, DateTime.Now.Day, 9, 0, 0);
            DateTime horaFin = new DateTime(DateTime.Now.Year, DateTime.Now.Month, DateTime.Now.Day, 21, 0, 0);

            for (DateTime hora = horaInicio; hora <= horaFin; hora = hora.AddMinutes(30))
            {
                hores.Add(hora.ToString(@"hh\:mm"));
            }

            return hores;
        }

        //public MyViewModelDB myViewModel { get; } = new MyViewModelDB();

    }
}
