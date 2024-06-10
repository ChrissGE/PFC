import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Auth, signInWithEmailAndPassword } from '@angular/fire/auth';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  constructor(private auth: Auth, private router: Router) {}
  onSubmit() {
    if (this.email && this.password) {
      console.log('Email:', this.email);
      console.log('Contraseña:', this.password);
      login(this.auth, this.email, this.password)
        .then((complete) => this.router.navigate(['/main/dashboard']))
        .catch((error) => console.log(error));
    } else {
      console.log(
        'Por favor, ingresa un correo electrónico y una contraseña válidos.'
      );
    }
  }
}

function login(auth: Auth, email: string, password: string) {
  return signInWithEmailAndPassword(auth, email, password);
}
