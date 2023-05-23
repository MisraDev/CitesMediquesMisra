using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AppEsciptoriAgendaVisitesMediques.model
{
    public class Hora
    {
        public Hora(String horaCita, bool dilluns, bool dimarts, bool dimecres, bool dijous, bool divendres, bool dissabre, bool diumenge)
        {
            HoraCita = horaCita;
            Dilluns = dilluns;
            Dimarts = dimarts;
            Dimecres = dimecres;
            Dijous = dijous;
            Divendres = divendres;
            Disabte = dissabre;
            Diumenge = diumenge;

            
        }

        public static List<Hora> getHores()
        {
            List<string> listaHoras = GetListaHoras();
            List<Hora> hores = new List<Hora>();
            hores.Add(new Hora(listaHoras[0], false, true, true, true, false, false, false));
            hores.Add(new Hora(listaHoras[1], false, true, true, true, false, false, false));
            hores.Add(new Hora(listaHoras[2], false, true, true, true, false, false, false));
            hores.Add(new Hora(listaHoras[3], false, true, true, true, false, false, false));
            hores.Add(new Hora(listaHoras[4], false, true, true, true, false, false, false));
            hores.Add(new Hora(listaHoras[5], false, true, true, true, false, false, false));
            return hores;
        }


        public String HoraCita { get; set; }

        public Boolean Dilluns{ get; set; }
        public Boolean Dimarts{ get; set; }
        public Boolean Dimecres { get; set; }
        public Boolean Dijous { get; set; }
        public Boolean Divendres { get; set; }
        public Boolean Disabte { get; set; }
        public Boolean Diumenge { get; set; }


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
    }

    

}
