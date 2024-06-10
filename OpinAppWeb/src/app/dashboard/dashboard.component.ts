import { Component } from '@angular/core';
import { scoresData } from './scores';
import { NgFor, NgIf } from '@angular/common';
@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [NgFor,NgIf],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  companyName='Ginos';
  email='ginos_parque_oeste@ginos.com';
  direccion='Av. de Europa, 7, 28922 Alcorcón, Madrid';
  cif='U31477490';
  tlfn='660982235';

  scoreGlobal=54;
  scoresData=scoresData;

  color(mark: number): string {
    const color = calcularColor(mark);
    if (color) {
      return `rgb(${color.join(',')})`;
    } else {
      return 'transparent'; // Devuelve transparente si no hay color calculado
    }
  }
  getStrokeArray(mark: number): string {
    const circumference = 251.327; // Circunferencia de un círculo con radio 40
    const percentage = mark / 100;
    const strokeLength = percentage * circumference;
    const gapLength = circumference - strokeLength;
    return `${strokeLength} ${gapLength}`;
  }
}

function interpolateColor(color1: number[], color2: number[], color3: number[], ratio: number): number[] {
  const inverseRatio = 1 - ratio;

  const r = Math.round(color1[0] * ratio + color2[0] * inverseRatio);
  const g = Math.round(color1[1] * ratio + color2[1] * inverseRatio);
  const b = Math.round(color1[2] * ratio + color2[2] * inverseRatio);

  const r2 = Math.round(color2[0] * ratio + color3[0] * inverseRatio);
  const g2 = Math.round(color2[1] * ratio + color3[1] * inverseRatio);
  const b2 = Math.round(color2[2] * ratio + color3[2] * inverseRatio);

  const finalR = Math.round(r * ratio + r2 * inverseRatio);
  const finalG = Math.round(g * ratio + g2 * inverseRatio);
  const finalB = Math.round(b * ratio + b2 * inverseRatio);

  return [finalR, finalG, finalB];
}

function calcularColor(mark: number): number[] | null {
  const startColor = [255, 0, 0];
  const centerColor = [255, 255, 0];
  const endColor = [0, 255, 0];

  if (mark >= 0 && mark <= 100) {
    const ratio = mark / 100;
    return interpolateColor(endColor, centerColor, startColor, ratio);
  } else {
    return null;
  }
}
