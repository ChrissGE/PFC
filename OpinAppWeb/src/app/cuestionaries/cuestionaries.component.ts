import { NgFor, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { questionsData } from './questions';
import { DataTablesModule } from 'angular-datatables';
import { conditionsData } from './conditions';

@Component({
  selector: 'app-cuestionaries',
  standalone: true,
  imports: [NgFor,NgIf,DataTablesModule],
  templateUrl: './cuestionaries.component.html',
  styleUrl: './cuestionaries.component.css'
})
export class CuestionariesComponent {
  sections = ['Preguntas', 'Ponderaciones de preguntas', 'Vista general'];
  sectionSelected:String='Preguntas';
  questions=questionsData
  conditions=conditionsData
  onSectionClick(section:String){
    this.sectionSelected=''
    this.sectionSelected=section;
    console.log(this.sectionSelected)
  }
  groupByQuestionaryMenu(questions: any[]) {
    const groupedQuestions: any = {};
    questions.forEach(question => {
      const key = question.questionaryMenu;
      if (!groupedQuestions[key]) {
        groupedQuestions[key] = [];
      }
      groupedQuestions[key].push(question);
    });
    return groupedQuestions;
  }
  getObjectKeys(obj: any) {
    return Object.keys(obj);
  }
}
