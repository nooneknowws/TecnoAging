//TODO: arrancar esse html estático que o Claude gerou
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Avaliacao } from '../../../_shared/models/avaliacao/avaliacao';
import { AvaliacaoService } from '../../../_shared/services/avaliacao.service';
import { Resposta } from '../../../_shared/models/avaliacao/resposta';

@Component({
  selector: 'app-editar-avaliacao',
  template: `
    <div class="container mt-4">
      <h2>Editar Avaliação</h2>
      
      <form [formGroup]="avaliacaoForm" (ngSubmit)="onSubmit()" *ngIf="avaliacao">
        <div class="card">
          <div class="card-header">
            <h5 class="mb-0">{{avaliacao.formulario?.titulo}}</h5>
          </div>
          <div class="card-body">
            <div class="row mb-3">
              <div class="col-md-6">
                <p><strong>Paciente:</strong> {{avaliacao.paciente?.nome}}</p>
              </div>
              <div class="col-md-6">
                <p><strong>Técnico:</strong> {{avaliacao.tecnico?.nome}}</p>
              </div>
            </div>

            <div formArrayName="respostas">
              <div *ngFor="let resposta of respostasFormArray.controls; let i = index" 
                   [formGroupName]="i" 
                   class="mb-3">
                <label [for]="'resposta' + i" class="form-label">
                  {{avaliacao.respostas![i].pergunta?.texto}}
                </label>
                <input [id]="'resposta' + i" 
                       type="text" 
                       class="form-control"
                       formControlName="valor">
              </div>
            </div>

            <div class="mt-4">
              <button type="submit" 
                      class="btn btn-primary me-2" 
                      [disabled]="!avaliacaoForm.valid || avaliacaoForm.pristine">
                Salvar
              </button>
              <button type="button" 
                      class="btn btn-secondary" 
                      (click)="cancelar()">
                Cancelar
              </button>
            </div>
          </div>
        </div>
      </form>
    </div>
  `
})
export class EditarAvaliacaoComponent implements OnInit {
  avaliacao?: Avaliacao;
  avaliacaoForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private avaliacaoService: AvaliacaoService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.avaliacaoForm = this.fb.group({
      respostas: this.fb.array([])
    });
  }

  get respostasFormArray() {
    return this.avaliacaoForm.get('respostas') as FormArray;
  }

  ngOnInit() {
    const avaliacaoId = this.route.snapshot.params['id'];
    if (avaliacaoId) {
      this.avaliacaoService.getAvaliacaoById(avaliacaoId).subscribe({
        next: (avaliacao) => {
          this.avaliacao = avaliacao;
          this.initializeForm();
        },
        error: (error) => {
          console.error('Erro ao carregar avaliação:', error);
        }
      });
    }
  }

  private initializeForm() {
    if (!this.avaliacao?.respostas) return;

    this.avaliacao.respostas.forEach(resposta => {
      this.respostasFormArray.push(
        this.fb.group({
          perguntaId: [resposta.pergunta?.id],
          valor: [resposta.resposta, Validators.required]
        })
      );
    });
  }

  onSubmit() {
    if (this.avaliacaoForm.valid && this.avaliacao) {
      const updatedAvaliacao = { ...this.avaliacao };
      updatedAvaliacao.respostas = this.respostasFormArray.controls.map((control, index) => {
        return new Resposta(
          this.avaliacao!.respostas![index].pergunta,
          control.value.valor
        );
      });

      this.avaliacaoService.salvarAvaliacao(updatedAvaliacao).subscribe({
        next: () => {
          this.router.navigate(['/avaliacoes/consultar']);
        },
        error: (error) => {
          console.error('Erro ao salvar avaliação:', error);
        }
      });
    }
  }

  cancelar() {
    this.router.navigate(['/avaliacoes/consultar']);
  }
}