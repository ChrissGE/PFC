import { Component } from '@angular/core';
import { rewards } from './rewards';
import { NgFor, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-rewards',
  standalone: true,
  imports: [NgFor,NgIf,FormsModule],
  templateUrl: './rewards.component.html',
  styleUrl: './rewards.component.css'
})
export class RewardsComponent {
  rewardsData=rewards;
  newReward = {
    image: 'favicon.ico',
    description: '',
    points: 0
  };
  showModal = false;

  openRewardModal() {
    this.showModal = true;
  }

  closeRewardModal() {
    this.showModal = false;
  }

  saveReward() {
    const confirmacion = window.confirm("¿Estás seguro de que quieres agregar este elemento?");
    if (confirmacion) {
      this.rewardsData.push(this.newReward);
    }
    this.newReward = {
      image: 'favicon.ico',
      description: '',
      points: 0
    };
    this.closeRewardModal();
  }

  borrarReward(index: number): void {
    const confirmacion = window.confirm("¿Estás seguro de que quieres borrar este elemento?");
    if (confirmacion) {
      this.rewardsData.splice(index, 1);
    }
  }
}
