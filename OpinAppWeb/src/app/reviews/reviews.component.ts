import { Component } from '@angular/core';
import { Review } from './review';
import { NgClass, NgFor } from '@angular/common';

@Component({
  selector: 'app-reviews',
  standalone: true,
  imports: [NgFor,NgClass],
  templateUrl: './reviews.component.html',
  styleUrl: './reviews.component.css'
})
export class ReviewsComponent {
  reviews: Review[] = [
    {
      name: 'Sarah Myers',
      username: 'hellosarahsound',
      rating: 2,
      comment: '¡Qué experiencia tan decepcionante! La comida en Ginos no cumplió con mis expectativas. La pasta estaba pasada y la salsa carecía de sabor.',
      date: 'hace 2 meses'
    },
    {
      name: 'Jón Gunnarsson',
      username: '',
      rating: 5,
      comment: '¡Una experiencia increíble! Ginos ofrece la mejor comida italiana que he probado. El ambiente es acogedor y el servicio, impecable.',
      date: 'hace 2 meses'
    },
    {
      name: 'Tracy Madler',
      username: '',
      rating: 4,
      comment: 'Acabamos de completar una cena de 10 platos en Ginos. ¡Qué maravilla! La comida fue excelente de principio a fin, aunque el servicio podría mejorar un poco.',
      date: 'hace 2 meses'
    },
    {
      name: 'Rotling Powers',
      username: '',
      rating: 3,
      comment: 'Primero: podemos recomendar Ginos al 100%. La comida estaba buena, pero el servicio fue lento y desorganizado.',
      date: 'hace 2 meses'
    },
    {
      name: 'Alex Johnson',
      username: 'alexj',
      rating: 5,
      comment: '¡Servicio fantástico! La comida estaba en excelente condición y el personal fue increíblemente amable. ¡Altamente recomendado!',
      date: 'hace 1 mes'
    },
    {
      name: 'Emily Stone',
      username: 'emilystone',
      rating: 4,
      comment: 'Buena experiencia en general. El proceso de reserva fue sencillo y la comida muy sabrosa. ¡Volveré a visitar Ginos!',
      date: 'hace 3 semanas'
    },
    {
      name: 'Michael Brown',
      username: '',
      rating: 3,
      comment: 'La comida estaba bien, pero el servicio al cliente podría haber sido mejor. Tuve algunos problemas con la reserva.',
      date: 'hace 1 mes'
    },
    {
      name: 'Linda Wilson',
      username: 'lindaw',
      rating: 4,
      comment: 'Buena relación calidad-precio. La comida estuvo bien durante nuestra visita. Lo único negativo fue el tiempo de espera para ser atendidos.',
      date: 'hace 2 semanas'
    }

  ];

}
