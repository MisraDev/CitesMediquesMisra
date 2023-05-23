using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AppEsciptoriAgendaVisitesMediques.model
{
    class MyViewModel
    {

        public List<string> Horas { get; } = GetListaHoras();
        public List<string> Fechas { get; } = ObtenirDatesDeLaSetmanaActual();
        public List<Cita> Citas { get; } = ObtenerCitas();

        public static List<Cita> ObtenerCitas()
        {
            Persona persona = new Persona(1, "12345678A", "Juan", "Pérez", "García", "Calle Mayor 1", "Barcelona", 1, "juanperez", "password", false);
            Persona metge = new Persona(2, "23456789B", "Pablo", "González", "Martínez", "Calle Gran 2", "Barcelona", 1, "pablogonzalez", "password", true);
            List<Cita> citas = new List<Cita>();
            citas.Add(new Cita(1, DateTime.Now, "sodhifdslkj", persona, metge));
            citas.Add(new Cita(1, new DateTime(2023, 5, 15, 10, 0, 0), "sodhifdslkj", persona, metge));
            citas.Add(new Cita(2, new DateTime(2023, 5, 16, 15, 30, 0), "sodhifdslkj", persona, metge));
            citas.Add(new Cita(3, new DateTime(2023, 5, 18, 11, 15, 0), "sodhifdslkj", persona, metge));
            citas.Add(new Cita(4, new DateTime(2023, 5, 19, 16, 0, 0), "sodhifdslkj", persona, metge));
            citas.Add(new Cita(5, new DateTime(2023, 5, 20, 9, 0, 0), "sodhifdslkj", persona, metge));

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
                try
                {
                    DateTime data = primerDiaDeLaSetmana.AddDays(i);
                    string dataFormatada = data.ToString("dd/MM/yyyy");
                    string nomDelDia = data.ToString("dddd", new CultureInfo("ca-ES"));
                    nomDelDia = char.ToUpper(nomDelDia[0]) + nomDelDia.Substring(1);
                    dates.Add(nomDelDia + "\n" + dataFormatada);
                }
                catch(Exception e)
                {
                    e.Message.ToString();
                }
                
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

        public MyViewModel myViewModel { get; } = new MyViewModel();

    }
}
