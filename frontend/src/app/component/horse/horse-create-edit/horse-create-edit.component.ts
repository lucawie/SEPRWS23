import {Component, OnInit} from '@angular/core';
import {NgForm, NgModel} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {Observable, of, retry} from 'rxjs';
import {Horse} from 'src/app/dto/horse';
import {Sex} from 'src/app/dto/sex';
import {HorseService} from 'src/app/service/horse.service';
import {Breed} from "../../../dto/breed";
import {BreedService} from "../../../service/breed.service";



export enum HorseCreateEditMode {
  create,
  edit,
  detail ,
}

@Component({
  selector: 'app-horse-create-edit',
  templateUrl: './horse-create-edit.component.html',
  styleUrls: ['./horse-create-edit.component.scss']
})
export class HorseCreateEditComponent implements OnInit {

  mode: HorseCreateEditMode = HorseCreateEditMode.create;
  horse: Horse = {
    name: '',
    sex: Sex.female,
    dateOfBirth: new Date(), // TODO this is bad
    height: 0, // TODO this is bad
    weight: 0, // TODO this is bad
  };

  private heightSet: boolean = false;
  private weightSet: boolean = false;
  private dateOfBirthSet: boolean = false;

  get height(): number | null {
    return this.heightSet
      ? this.horse.height
      : null;
  }

  set height(value: number) {
    this.heightSet = true;
    this.horse.height = value;
  }

  get weight(): number | null {
    return this.weightSet
      ? this.horse.weight
      : null;
  }

  set weight(value: number) {
    this.weightSet = true;
    this.horse.weight = value;
  }

  get dateOfBirth(): Date | null {
    return this.dateOfBirthSet
      ? this.horse.dateOfBirth
      : null;
  }

  set dateOfBirth(value: Date) {
    this.dateOfBirthSet = true;
    this.horse.dateOfBirth = value;
  }


  constructor(
    private service: HorseService,
    private breedService: BreedService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
  ) {
  }

  public get heading(): string {
    switch (this.mode) {
      case HorseCreateEditMode.create:
        return 'Create New Horse';
      case HorseCreateEditMode.edit:
        return 'Edit your Horse';
      case HorseCreateEditMode.detail:
        return 'Details of your Horse';
      default:
        return '?';
    }
  }

  public get submitButtonText(): string {
    switch (this.mode) {
      case HorseCreateEditMode.create:
        return 'Create';
      case HorseCreateEditMode.edit:
        return 'Edit';
      case HorseCreateEditMode.detail:
        return 'Edit this Horse';
      default:
        return '?';
    }
  }

  get modeIsCreate(): boolean {
    return this.mode === HorseCreateEditMode.create;
  }

  get modeIsEdit(): boolean {
    return this.mode === HorseCreateEditMode.edit;
  }

  get modeIsDetail(): boolean {
    return this.mode === HorseCreateEditMode.detail;
  }


  get sex(): string {
    switch (this.horse.sex) {
      case Sex.male: return 'Male';
      case Sex.female: return 'Female';
      default: return '';
    }
  }

  private get modeActionFinished(): string {
    switch (this.mode) {
      case HorseCreateEditMode.create:
        return 'created';
      case HorseCreateEditMode.edit:
        return 'edited';
      default:
        return '?';
    }
  }

  //method for switching from deatil to edit mode
  switchToEditMode() {
    if (this.horse && this.horse.id !== undefined) {
      this.mode = HorseCreateEditMode.edit;
      this.router.navigate(['/horses', this.horse.id, 'edit']);
    } else {
      this.notification.error("Unable to switch to edit mode. Horse with given ID is not available.");
    }
  }

  public delete() {
    const observable = this.service.delete(Number(this.horse.id));
    observable.subscribe({
      next: () => {
        this.notification.success('Horse ' + this.horse.name + ' deleted successfully');
        this.router.navigate(['/horses']).then(() => console.log('Navigated to horses main page'));
      },
      error: error => {
        console.error('Error deleting horse', error);
        this.notification.error('Error deleting horse. Please try again later', 'Error');
      }
    });
  }

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.mode = data.mode;
    });

    if(this.modeIsEdit || this.modeIsDetail){
      this.route.params.subscribe((params = {}) => {
        this.service.getById(Number(params.id))
          .subscribe({
            next: data => {
              this.horse = data;
              this.dateOfBirth = this.horse.dateOfBirth;
              this.weight = data.weight;
              this.height = data.height;
              //console.log("data:", data);
            },
            error: () => {
              this.notification.error('Is the backend running?', 'Unable to load horse data');
              console.log('Could not load horse.\nAre all systems Running?');
              this.router.navigate(['/horses']).then(() => console.log('Navigated to horses main page'));
            }
          })
      });
    }
  }

  public dynamicCssClassesForInput(input: NgModel): any {
    return {
      'is-invalid': !input.valid && !input.pristine,
    };
  }

  public formatBreedName(breed: Breed | null): string {
    return breed?.name ?? '';
  }

  breedSuggestions = (input: string) => (input === '')
    ? of([])
    :  this.breedService.breedsByName(input, 5);

  public onSubmit(form: NgForm): void {
    if (form.valid) {
      console.log('is form valid?', form.valid, this.horse);
      let observable: Observable<Horse>;
      switch (this.mode) {
        case HorseCreateEditMode.create:
          console.log(this.horse);
          observable = this.service.create(this.horse);
        case HorseCreateEditMode.edit:
          observable = this.service.edit(this.horse);
          break;
        default:
          console.error('Unknown HorseCreateEditMode', this.mode);
          return;
      }
      observable.subscribe({
        next: data => {
          this.notification.success(`Horse ${this.horse.name} successfully ${this.modeActionFinished}.`);
          this.router.navigate(['/horses']);
        },
        error: error => {
          console.error('Error creating horse', error);
          // TODO show an error message to the user. Include and sensibly present the info from the backend!
        }
      });
    }
  }

}
