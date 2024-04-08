import {Component, OnInit} from '@angular/core';
import {debounceTime, Subject, take} from "rxjs";
import {TournamentDetailDto, TournamentListDto, TournamentSearchParams} from "../../dto/tournament";
import {ToastrService} from "ngx-toastr";
import {TournamentService} from "../../service/tournament.service";

@Component({
  selector: 'app-tournament',
  templateUrl: './tournament.component.html',
  styleUrls: ['./tournament.component.scss']
})

export class TournamentComponent implements OnInit {

  search = false;
  tournaments: TournamentListDto[] = [];
  bannerError: string | null = null;
  searchParams: TournamentSearchParams = {};
  searchChangedObservable = new Subject<void>();
  startDate: string | null = null;
  endDate: string | null = null;

  constructor(
    private service: TournamentService,
    private notification: ToastrService,
  ) {
  }


  ngOnInit(): void {
    this.reloadTournaments();

    this.searchChangedObservable
      .pipe(debounceTime(300))
      .subscribe({next: () => this.reloadTournaments()});
  }

  reloadTournaments() {
    if (this.startDate == null || this.startDate === "") {
      delete this.searchParams.startDate;
    } else {
      this.searchParams.startDate = new Date(this.startDate);
    }
    if (this.endDate == null || this.endDate === "") {
      delete this.searchParams.endDate;
    } else {
      this.searchParams.endDate = new Date(this.endDate);
    }
    this.service.search(this.searchParams)
      .pipe(take(1))
      .subscribe({
        next: data => {
          this.tournaments = data;
        },
        error: error => {
          console.error('Error fetching tournaments', error);
          this.bannerError = 'Could not fetch tournaments: ' + error.message;
          const errorMessage = error.status === 0
            ? 'Is the backend up?'
            : error.message.message;
          this.notification.error(errorMessage, 'Could Not Fetch Tournaments');
        }
      });


  }

  searchChanged(): void {
    console.log("Search parameters: ", this.searchParams);
    this.searchChangedObservable.next();
  }

}
