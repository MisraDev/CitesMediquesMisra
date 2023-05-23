using DbLib.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AppEsciptoriAgendaVisitesMediques.model
{
    public class Hora
    {
        public enum Dies { Dilluns, Dimarts, Dimecres, Dijous, Divendres, Dissabte, Diumenge }
        public enum Hores
        {
            H07M00, H07M30, H08M00, H08M30, H09M00, H09M30, H10M00, H10M30, H11M00, H11M30, H12M00, H12M30,
            H13M00, H13M30, H14M00, H14M30, H15M00, H15M30, H16M00, H16M30, H17M00, H17M30, H18M00, H18M30, H19M00, H19M30, H20M00, H20M30
        }

        public string HoraCol { get; set; }
        public CitaDB Dilluns { get; set; }
        public CitaDB Dimarts { get; set; }
        public CitaDB Dimecres { get; set; }
        public CitaDB Dijous { get; set; }
        public CitaDB Divendres { get; set; }
        public CitaDB Dissabte { get; set; }
        public CitaDB Diumenge { get; set; }


        public Hora(string Hora)
        {
            this.HoraCol = Hora;
        }
        public Hora(string Hora, CitaDB Dilluns, CitaDB Dimarts, CitaDB Dimecres, CitaDB Dijous, CitaDB Divendres, CitaDB Dissabte, CitaDB Diumenge)
        {
            this.HoraCol = Hora;
            if (Dilluns != null)
            {
                this.Dilluns = Dilluns;
            }
            if (Dimarts != null)
            {
                this.Dimarts = Dimarts;
            }
            if (Dimecres != null)
            {
                this.Dimecres = Dimecres;
            }
            if (Dijous != null)
            {
                this.Dijous = Dijous;
            }
            if (Divendres != null)
            {
                this.Divendres = Divendres;
            }
            if (Dissabte != null)
            {
                this.Dissabte = Dissabte;
            }
            if (Diumenge != null)
            {
                this.Diumenge = Diumenge;
            }
        }

        public static List<Hora> GenerarHorari()
        {
            var horariList = new List<Hora>();
            foreach (Hores h in Enum.GetValues(typeof(Hores)))
            {
                var horari = new Hora(convertHora(h), null, null, null, null, null, null, null);
                horariList.Add(horari);
            }

            return horariList;
        }

        private static string convertHora(Hores h)
        {
            string hora = h.ToString();

            // Obtenir el número d'horas i minuts de la string
            int horas = int.Parse(hora.Substring(1, hora.Contains("M") ? hora.IndexOf("M") - 1 : 1));
            int minutos = int.Parse(hora.Substring(hora.Contains("M") ? hora.IndexOf("M") + 1 : 3, 2));

            // Combinar les hores i els minuts en una string amb format HH:MM
            return $"{horas}:{minutos:D2}";
        }
    }

    

}
