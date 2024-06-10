import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { initializeApp, provideFirebaseApp } from '@angular/fire/app';
import { getAuth, provideAuth } from '@angular/fire/auth';


export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideClientHydration(),
    importProvidersFrom(
      provideFirebaseApp(() =>
        initializeApp({
          projectId: 'opinapp-1d6bc',
          appId: '1:670051286413:web:3ccfe2d6832b3fa105d1c4',
          storageBucket: 'opinapp-1d6bc.appspot.com',
          apiKey: 'AIzaSyBwM-7lvsLZHJ3QtL8S-otPs0cvrSo-vMM',
          authDomain: 'opinapp-1d6bc.firebaseapp.com',
          messagingSenderId: '670051286413',
          measurementId: 'G-ZKFEB2P4W9',
        })
      )
    ),
    importProvidersFrom(provideAuth(() => getAuth())),
  ],
};
